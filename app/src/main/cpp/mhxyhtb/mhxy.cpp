//
// Created by beijiuhu on 18-5-7.
//



#include <CydiaSubstrate.h>
#include <string>
#include <list>
#include <jni.h>
#include <dlfcn.h>
#include <stddef.h>
#include <fcntl.h>
#include <dirent.h>
#include <unistd.h>
#include <sys/system_properties.h>
#include <utils/Util.h>

#include "SymbolFinder.h"



#define HOOK_DEF(ret, func, ...) \
  static ret (*orig_##func)(__VA_ARGS__); \
  static ret new_##func(__VA_ARGS__)

int findSymbol(const char* name, const char* libn, unsigned long* addr) {
    return find_name(getpid(), name, libn, addr);
}

void write_file(const char *name, const char *buf, unsigned int datalen) {
    FILE *file = fopen(name, "wb");
    if (file == nullptr) {
        LOGD("Unable to open file %s", name);
        return;
    }

    size_t write_len = fwrite(buf, 1, datalen, file);
    fclose(file);
    LOGD("write_file ok %s, %d", name, write_len);
}

void read_file(const char* name, char** data_buf, uint32_t* data_len) {
    FILE* file;
    char* buffer;
    unsigned long fileLen;

    //Open file
    file = fopen(name, "rb");
    if (!file) {
        fprintf(stderr, "Unable to open file %s", name);
        return;
    }

    //Get file length
    fseek(file, 0, SEEK_END);
    fileLen = ftell(file);
    fseek(file, 0, SEEK_SET);

    //Allocate memory
    buffer = (char*) malloc(fileLen + 1);
    if (!buffer) {
        fprintf(stderr, "Memory error!");
        fclose(file);
        return;
    }

    //Read file contents into buffer
    fread(buffer, fileLen, 1, file);
    fclose(file);

    //Do what ever with buffer
    *data_buf = buffer;
    *data_len = fileLen;
}

// funciton pointer

bool just_once = true;

void* (* g_x)(const char* str, int start, void* globals, void* locals, void* flags);

const char tmp_script[] = "import marshal\ninfile = '/sdcard/fixmy.py'\noutfile = '/sdcard/fixmy.pyc'\ncontent = open(infile, 'rb').read()\nout_fd = open(outfile, 'wb')\ncobj = compile(content, '', 'exec')\nmarshal.dump(cobj, out_fd)\nout_fd.close()";

HOOK_DEF(void*, simple_run, const char* str, int start, void* globals, void* locals, void* flags) {
    LOGD("im simple_run ..%d %s", start, str);

    if (just_once) {
        just_once = false;

        void* r = orig_simple_run(str, start, globals, locals, flags);

        char* data_buf;
        uint32_t mlen = 0;
        read_file("/sdcard/fixmy.pyc", &data_buf, &mlen);
        if (mlen == 0) {
            LOGD("fixmy.pyc not exists, create fixmy.pyc..");
            //system("su -c \"am force-stop  com.netease.dwrg\"");
            g_x(tmp_script, 257, globals, locals, flags);
        }

        return r;
    }

    return orig_simple_run(str, start, globals, locals, flags);
}

HOOK_DEF(void*, PyMarshal_ReadObjectFromString, const char* data, unsigned int len) {
    LOGD("im here.. %d", len);

    if (len > 2390 && len <= 3390) {
        LOGD("modify orig my ");
        char* data_buf;
        uint32_t mlen = 0;

        write_file("/sdcard/orig_my.pyc", data, len);

        read_file("/sdcard/fixmy.pyc", &data_buf, &mlen);
        if (mlen == 0) {
            LOGE("fix_redirect.pyc not found, call default..");
            return orig_PyMarshal_ReadObjectFromString(data, len);
        }

        LOGD("/////////////////// use our fixed my ///////////////////");
        return orig_PyMarshal_ReadObjectFromString(data_buf, mlen);
    }

    return orig_PyMarshal_ReadObjectFromString(data, len);
}

size_t get_library_address(const char* libname) {
    char path[256];
    char buff[256];

    FILE* file;
    size_t addr = 0;

    snprintf(path, sizeof path, "/proc/%d/smaps", getpid());
    LOGI("MY PID:%d %s", getpid(), path);
    file = fopen(path, "rt");
    if (file == NULL) {
        LOGE("get_library_address open file failed!");
        return 0;
    }

    while (fgets(buff, sizeof buff, file) != NULL) {
        if (strstr(buff, libname) == NULL) {
            continue;
        }

        size_t start, end, offset;
        char flags[4];
        if (sscanf(buff, "%zx-%zx %c%c%c%c %zx", &start, &end, &flags[0], &flags[1], &flags[2], &flags[3], &offset) != 7) {
            continue;
        }

        LOGD("%s", buff);
        LOGE("ARG:%zx %zx %c %c %c %c %zx", start, end, flags[0], flags[1], flags[2], flags[3], offset);

        if (flags[0] != 'r' || flags[2] != 'x') {
            continue;
        }

        if (offset != 0) {
            continue;
        }
        //if (offset == 0) {

        addr = start - offset;
        //}
        break;
    }

    fclose(file);
    return addr;
}


void __attribute__ ((constructor)) libmain() {
    unsigned int baddr = get_library_address("libclient.so");
    LOGD("base addr 0x%08x", baddr);
    if (baddr == 0) {
        LOGD("get_library_address return 0, just return");
        return;
    }
    //base = get_module_base(getpid(), "libSecurity.so");

    void *symbol = nullptr;
    symbol = reinterpret_cast<void *>((uint32_t) baddr + 0x1241394 );
    if (symbol!= 0 ) {
        LOGD("FIND PyMarshal_ReadObjectFromString FUCK IT ：%p", symbol);
        MSHookFunction(symbol, (void *) new_PyMarshal_ReadObjectFromString, (void **) &orig_PyMarshal_ReadObjectFromString);
        //MSHookFunction(symbol, (void *) new_jiemi, (void **) &orig_jiemi);
        LOGD("HOOK PyMarshal_ReadObjectFromString ok!");
    }





    g_x = reinterpret_cast<void* (*)(const char*, int, void*, void*, void*)>(0x1264a40+(long) baddr);
    LOGD("[+]FIND PyRun_StringFlags FUCK IT ：%p", g_x);
    MSHookFunction((void*) g_x, (void*) new_simple_run, (void**) &orig_simple_run);
    LOGD("[+]HOOK PyRun_StringFlags ok!");
//    unsigned int execModule = 0xc5d16000 + 0x00e9b5b8 - 0xc5d02000 + (long) baddr;
//    unsigned int read4str = 0xc5d16000 + 0x00ea30dc - 0xc5d02000 + (long) baddr;

//    LOGD("execModule %08x", execModule);
//    LOGD("read4str %08x", read4str);
//    LOGD("g_x %08x", (unsigned int) g_x);

//    MSHookFunction((void*) read4str, (void*) new_PyMarshal_ReadObjectFromString, (void**) &orig_PyMarshal_ReadObjectFromString);

}
