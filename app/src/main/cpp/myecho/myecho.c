/* myecho.c */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

int main(int argc, char *argv[]) {
    int j;

    //for (j = 0; j < argc; j++)
    //    printf("argv[%d]: %s\n", j, argv[j]);
    unsigned int index; // r0
    unsigned int v4; // r4
    char v6[16]; // [sp+0h] [bp-78h]
    int v7; // [sp+4h] [bp-74h]
    int v8; // [sp+8h] [bp-70h]
    int v9; // [sp+Ch] [bp-6Ch]
    char v10; // [sp+10h] [bp-68h]
    char alph[62]; // [sp+14h] [bp-64h]
    time_t v2;
    strcpy(alph, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    //memset(v6, NULL,16);
    v7 = 0;
    v8 = 0;
    v9 = 0;
    v10 = 0;
    v2 = time(0);///seed
    srand48(v2);///rand generate
    //v4 = (char *) v6;
    v4 = 0;
    for (int i = 0; i < 16; ++i) {
        index = lrand48() / 0x2108421u;
        v6[i] = alph[index];
        //++v4;
    }


    printf("gen :%s,%d\n", v6, strlen(v6));
    exit(EXIT_SUCCESS);
}