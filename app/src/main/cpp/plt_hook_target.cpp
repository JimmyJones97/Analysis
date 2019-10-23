#include <cstdlib>
#include <cstdio>


void say_hello()
{
    char *buf =(char *) malloc(1024);
    if(nullptr != buf)
    {
        snprintf(buf, 1024, "%s", "hello\n");
        printf("%s", buf);
    }
}