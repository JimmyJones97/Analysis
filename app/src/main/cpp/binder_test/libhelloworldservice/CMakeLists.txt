cmake_minimum_required(VERSION 3.4.1)
project(libsrv)
file(GLOB lib_src  ${PROJECT_SOURCE_DIR}/src/*.cpp)




add_definitions(-DPLATFORM_ANDROID)
message(STATUS "源文件:${lib_src}")
add_library(${PROJECT_NAME} STATIC ${lib_src})

target_include_directories(${PROJECT_NAME}
        INTERFACE
        $<BUILD_INTERFACE:${PROJECT_SOURCE_DIR}/include/>
        $<INSTALL_INTERFACE:include>)

target_link_libraries(${PROJECT_NAME} log ui uitls)