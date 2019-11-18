project(helloworld C)



set(SRC_LIST hello.c)
set(SRC_LIB_LIST libhelloworld.c)
add_executable(${PROJECT_NAME} ${SRC_LIST})
add_library(helloworldlib SHARED ${SRC_LIB_LIST})
