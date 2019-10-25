

#### 
com.Autel.maxi

libMaxiDas.so

_ZN10CDataSheet8SearchIdE7CBinary


/sdcard/Scan/Asia/  libDiag.so --->diagnastic ```诊断功能```

libComm.so ```公用的库 ,通信用 send_receive_frame;send_receive_binary``` 

1|root@maxisys906s:/proc/28947 # cat maps |grep Das
784fe000-785ba000 rwxp 00000000 b3:03 1733470    /data/app-lib/com.Autel.maxi-2/libMaxiDas.so
78641000-78644000 rw-p 000bc000 b3:03 1733470    /data/app-lib/com.Autel.maxi-2/libMaxiDas.so


static main(void)
{
  auto fp, begin, end, dexbyte;
  fp = fopen("C:\\dump.dex", "wb");
  
  begin = 0x784fe000;
  size=0x146000;
  end = begin + size;
  for ( dexbyte = begin; dexbyte < end; dexbyte ++ )
      fputc(Byte(dexbyte), fp);
}


SwitchyOmega

./SoFixer  -m 0x784a5000 -s dasdas.dump -o laji.so


0x781F7EEC


_ZN10CDataSheet10DecrypTextEPhi
_ZN10CDataSheet10DecrypTextEPhi


_ZN10CDataSheet10DecrypTextEPhi


CEcuInterface::SendReceive(CSendFrame)	libComm.so:CEcuInterface::SendReceive(CSendFrame)
CEcuInterface::SendReceive(CBinary)	libComm.so:CEcuInterface::SendReceive(CBinary)

CUITroubleCode::Add 故障码
CUIEcuInfo::Add	基本信息

logcat -s LIB_PASSTHRU


0x7db7c000   honda_diag.dump
LHGCM452232000001

