//
// Created by pwd61 on 2019/5/7.
//

#include "cmb.hpp"
#include "cmb_main.hpp"
#include "Util.h"


void  init_entry()__attribute__((constructor))
{
    int v19,v18,v14=0,v15;
    v19 = get_so_addr("/system/lib/libart.so");
    LOGS("2g.out", "loadbuf = %x", v19);
    v18 = *(int32_t *)(v19 + 28) + v19;

    for ( signed int i = 0; i < *(int16_t *)(v19 + 44); ++i )
    {
        if ( *(int32_t *)(v18 + 32 * i) == 2 )
        {
            v19 -= v14;
            v15 = v19 + *(int32_t *)(v18 + 32 * i + 8);
            LOGS("2g.out", "wwm:dynamic=%x %p\n", *(int32_t *)(v18 + 32 * i + 8), v15);
            break;
        }
        if ( *(int32_t *)(v18 + 32 * i) == 6 )
        {
            v14 = *(int32_t *)(v18 + 8) - *(int32_t *)(v18 + 4);
            LOGS("2g.out", "wwm:fix=%x\n", v14);
        }
    }

}