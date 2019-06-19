/* myecho.c */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

int sub_F658(int result, int a2, char a3);

int main(int argc, char *argv[]) {
    char *s = (char *) malloc(19u);
    memset(s, 0, 19u);//
    sub_F658((int) s, 16, 250);


    exit(EXIT_SUCCESS);
}


int sub_F658(int result, int a2, char a3) {
    int v3; // r4
    char v4; // r5
    signed int v5; // r3

    v5 = 0;
    def_F668:
    while (v5 != 5) {
        while (2) {
            switch (v5) {
                case 0:
                    v3 = 0;
                    v4 = *(char *) (result + 1) ^ a3;
                    goto LABEL_12;
                case 1:
                    *(char *) (result + v3) = 0;
                    return result;
                case 2:
                    *(char *) (result + v3) = *(char *) (result + v3 + 2) ^ v4;
                    v5 = 4;
                    continue;
                case 3:
                    if (v3 >= a2)
                        v5 = 1;
                    else
                        v5 = 2;
                    goto def_F668;
                case 4:
                    ++v3;
                LABEL_12:
                    v5 = 3;
                    continue;
                default:
                    goto def_F668;
            }
        }
    }
    return result;
}
