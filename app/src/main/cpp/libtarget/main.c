int mzhengHook(char * str){
    printf("mzheng Hook pid = %d\n", getpid());
    printf("Hello %s\n", str);
    LOGD("mzheng Hook pid = %d\n", getpid());
    LOGD("Hello %s\n", str);
    return 0;
}