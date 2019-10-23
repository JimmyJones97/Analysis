
#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <sys/mman.h>
#include <sys/user.h>
#include <cinttypes>
#include <plt_hook_target.h>

#define PAGE_START(addr) ((addr) & PAGE_MASK)
#define PAGE_END(addr)   (PAGE_START(addr) + PAGE_SIZE)

void *my_malloc(size_t size)
{
    printf("%zu bytes memory are allocated by libtest.so\n", size);
    return malloc(size);
}

void hook()
{
    char       line[512];
    FILE      *fp;
    uintptr_t  base_addr = 0;
    uintptr_t  addr;

    //find base address of libtest.so
    if(NULL == (fp = fopen("/proc/self/maps", "r"))) return;
    while(fgets(line, sizeof(line), fp))
    {
        if(NULL != strstr(line, "libpltfun.so") &&
           sscanf(line, "%" PRIxPTR "-%*lx %*4s 00000000", &base_addr) == 1)
            break;
    }
    fclose(fp);
    if(0 == base_addr) return;

    //the absolute address from got table.
    addr = base_addr + 0x3fbc;

    //add write permission
    mprotect((void *)PAGE_START(addr), PAGE_SIZE, PROT_READ | PROT_WRITE);

    //replace the function address
    *(void **)addr = (void**)my_malloc;

    //clear instruction cache
    __builtin___clear_cache((char *)PAGE_START(addr), (char *)PAGE_END(addr));
}

int main()
{
    hook();
    say_hello();
    return 0;
}