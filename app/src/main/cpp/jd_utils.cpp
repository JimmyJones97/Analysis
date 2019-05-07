//
// Created by pwd61 on 2019/4/17.
//
#include <cstdint>
#include <pthread.h>
#include "utils/Util.h"
#include "native-lib.h"
#include "jd_utils.h"

void *decryptMSG(void *) {
    uint32_t offset;
    offset = (uint32_t) base + 0x000173B0;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 16));
    offset = (uint32_t) base + 0X000179CC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X00017004;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 8));
    offset = (uint32_t) base + 0X00017024;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 38));
    offset = (uint32_t) base + 0X000170BC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 3));
    offset = (uint32_t) base + 0X000170C8;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 38));
    offset = (uint32_t) base + 0X00017160;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 12));
    offset = (uint32_t) base + 0X00017190;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 111));
    offset = (uint32_t) base + 0X000173F0;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 3));
    offset = (uint32_t) base + 0X000173FC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 1));
    offset = (uint32_t) base + 0X0001734C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 25));
    offset = (uint32_t) base + 0X00017400;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 27));
    offset = (uint32_t) base + 0X0001746C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 11));
    offset = (uint32_t) base + 0X00017498;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 49));
    offset = (uint32_t) base + 0X0001755C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 4));
    offset = (uint32_t) base + 0X0001756C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X00017584;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X0001759C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 22));
    offset = (uint32_t) base + 0X000175F4;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 11));
    offset = (uint32_t) base + 0X00017620;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 51));
    offset = (uint32_t) base + 0X000176EC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 7));
    offset = (uint32_t) base + 0X00017708;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 20));
    offset = (uint32_t) base + 0X00017758;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 14));
    offset = (uint32_t) base + 0X00017790;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 7));
    offset = (uint32_t) base + 0X000177AC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 3));
    offset = (uint32_t) base + 0X000177B8;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 28));
    offset = (uint32_t) base + 0X00017828;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X00017840;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 22));
    offset = (uint32_t) base + 0X00017898;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 16));
    offset = (uint32_t) base + 0X000178D8;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 5));
    offset = (uint32_t) base + 0X000178EC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 20));
    offset = (uint32_t) base + 0X0001793C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 3));
    offset = (uint32_t) base + 0X00017948;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 33));
    offset = (uint32_t) base + 0X000179CC;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X000179E4;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 5));
    offset = (uint32_t) base + 0X000179F8;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 31));
    offset = (uint32_t) base + 0X00017A74;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    offset = (uint32_t) base + 0X00017A8C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 23));
    offset = (uint32_t) base + 0X00017AE8;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 19));
    offset = (uint32_t) base + 0X00017B34;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 11));
    offset = (uint32_t) base + 0X00017B60;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 41));
    offset = (uint32_t) base + 0X00017C04;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 4));
    offset = (uint32_t) base + 0X00017C14;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base,
         JIEMI((void *) offset, 66));
    offset = (uint32_t) base + 0X00017D1C;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 7));
    offset = (uint32_t) base + 0X00017D38;
    LOGD("解密这个内容：%p-%x , %s", (void *) offset, offset - (uint32_t) base, JIEMI((void *) offset, 6));
    pthread_exit(nullptr);
}