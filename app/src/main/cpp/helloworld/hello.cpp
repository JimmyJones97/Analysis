
#include <stdio.h>
#include <unistd.h>
#include "hello.h"
#include <tools.h>



int main() {
    do {
        MYLOGD("Hello world! from binary ");
        LOGD("Hello world! from static lib ");
        printf("hello world!\n");
        sleep(5);///sleep 1s;
    } while (1);
    return 0;
}