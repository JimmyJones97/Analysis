project(helloworld )

set(SRC_LIST hello.cpp)
set(SRC_LIB_LIST libhelloworld.c)
include_directories(  ${CMAKE_CURRENT_SOURCE_DIR}/)
add_executable(${PROJECT_NAME} ${SRC_LIST})
target_compile_options(${PROJECT_NAME}
        BEFORE
        PRIVATE -DLOG_TAG="HELLO")
target_link_libraries(${PROJECT_NAME} tools log)


add_library(helloworldlib SHARED ${SRC_LIB_LIST})
