#include <iostream>
int hook_dlopen(const char* module_name, void *base_addr)
{
    //保存系统中原来dlopen函数地址，在Android.mk中加入库
    old_dlopen = dlopen;
    LOGD(ANDROID_LOG_DEBUG, "[+] Orig dlopen = %p\n", old_dlopen);
    base_addr = get_module_base(getpid(), module_name); //动态库地址
    LOGD(ANDROID_LOG_DEBUG, "[+] %s at %p\n", module_name, base_addr);
    if(base_addr == NULL) return 0;

    FILE *fpDll;
    fpDll = fopen(module_name, "r");
    if(fpDll == NULL)
    {
        LOGD(ANDROID_LOG_ERROR, "[-] fopen error\n");
        return 0;
    }
    Elf32_Ehdr ehdr;  //ELF header
    fread(&ehdr, 1, sizeof(Elf32_Ehdr), fpDll); //读取ELF文件格式的文件头信息

    unsigned long shdr_addr = ehdr.e_shoff; //section header table文件中的偏移
    int shnum = ehdr.e_shnum; //section header table中有多少个条目
    int shent_size = ehdr.e_shentsize; //section header table每一个条目的大小
    unsigned long stridx = ehdr.e_shstrndx; //包含节名称的字符串是第几个节(从0开始)

    Elf32_Shdr shdr; //节头结构定义
    fseek(fpDll, shdr_addr + stridx * shent_size, SEEK_SET); //偏移到文件尾
    fread(&shdr, 1, shent_size, fpDll); //读取字符串表的信息

    char* string_table = (char*)malloc(shdr.sh_size);//分配内存
    fseek(fpDll, shdr.sh_offset, SEEK_SET);//偏移到字符串表
    fread(string_table, 1, shdr.sh_size, fpDll); //读取字符串表的内容
    fseek(fpDll, shdr_addr, SEEK_SET);//还原指针到section header table处

    //计算文件virtaddr，即映像文件的基址image_base
    fread(&shdr, 1, shent_size, fpDll);
    fread(&shdr, 1, shent_size, fpDll);
    uint32_t image_base = shdr.sh_addr - shdr.sh_offset;
    LOGD(ANDROID_LOG_DEBUG, "[+] image_base = %x\n", image_base);
    fseek(fpDll, shdr_addr, SEEK_SET);//还原指针到section header table处

    int i;
    uint32_t out_addr = 0;
    uint32_t out_size = 0;
    uint32_t got_item = 0;
    int32_t got_found = 0;

    fread(&shdr, 1, shent_size, fpDll);
    for(i = 0; i < shnum; i++)
    {//每个节头信息,找到got表
        fread(&shdr, 1, shent_size, fpDll);
        if(shdr.sh_type == SHT_PROGBITS)
        {
            int name_idx = shdr.sh_name;//名称索引
            if(strcmp(&(string_table[name_idx]), ".got") == 0
               || strcmp(&(string_table[name_idx]), ".got.plt") == 0)
            {
                out_addr = (uint32_t)base_addr + shdr.sh_addr - image_base;//获得got表
                out_size = shdr.sh_size;
                LOGD(ANDROID_LOG_DEBUG, "[+] out_addr = %lx, out_size = %lx\n", out_addr, out_size);

                for(i = 0; i < out_size; i += 4)
                {
                    got_item = *(uint32_t*)(out_addr + i);
                    if(got_item == (uint32_t)old_dlopen)
                    {
                        LOGD(ANDROID_LOG_DEBUG, "[+] Found dlopen in GOT !\n");
                        got_found++;
                        //hook
                        uint32_t page_size = getpagesize();
                        uint32_t entry_page_start = (out_addr + i) & (~(page_size-1));
                        mprotect((uint32_t*)entry_page_start, page_size, PROT_READ | PROT_WRITE);
                        *(uint32_t*)(out_addr + i) = (uint32_t)new_dlopen;
                    }
                    else if(got_item == (uint32_t)new_dlopen)
                    {
                        LOGD(ANDROID_LOG_ERROR, "[-] Already hooked dlopen\n");
                    }
                }
                if(got_found)
                    break;
                else
                    LOGD(ANDROID_LOG_ERROR, "[-] not FOUND !!!\n");
            }
        }
    }
    free(string_table);
    fclose(fpDll);

    if(got_found)
        return 1;
    else
        return 0;
}