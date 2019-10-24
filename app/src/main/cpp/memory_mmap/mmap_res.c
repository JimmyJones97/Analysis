#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <string.h>

#define MAP_SZ       8192
#define MAP_FILE_SZ  (4096 - 10)
#define REAL_FILE_SZ (4096 - 10)

int main(int argc, char *argv[])
{
    int fd = open("./data", O_RDWR | O_CREAT | O_TRUNC, 0644);
    ftruncate(fd, REAL_FILE_SZ);

    void *addr = mmap(NULL, MAP_SZ, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd, 0);
    memset(addr + MAP_FILE_SZ, 0, MAP_SZ - MAP_FILE_SZ);
    printf("memset OK!\n");

    munmap(addr, MAP_SZ);
    close(fd);

    printf("everything OK!\n");
    return 0;
}