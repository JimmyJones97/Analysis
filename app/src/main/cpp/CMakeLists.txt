cmake_minimum_required(VERSION 3.4.1)
project(native-lib)

#子目录
add_subdirectory(slua)
add_subdirectory(debugger)
add_subdirectory(memdump)
add_subdirectory(meta_program)
add_subdirectory(cmb)
add_subdirectory(utils)
#substrate源文件
aux_source_directory(${PROJECT_SOURCE_DIR}/Substrate SUBSTATE_SRC)

file(GLOB SRC_TEST ${PROJECT_SOURCE_DIR}/Substrate/*.cpp)

#头文件
include_directories(
        ${PROJECT_SOURCE_DIR}

        ${PROJECT_SOURCE_DIR}/Substrate
)

#abi设置
if (${ANDROID_ABI} STREQUAL "x86")
    message(STATUS "X86 BUILD ")
    add_definitions(-D__i386__)
    remove_definitions(-D__arm__)
else ()
    message(STATUS "ARM BUILD")
    #message(STATUS "${COMPILE_DEFINITIONS}")
endif ()
#特殊宏
#add_definitions(-DCMAKE_EXPORT_COMPILE_COMMANDS=1)



find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
#
add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        native-lib.cpp
        jd_utils.cpp

        #utils/Util.cpp
        ${SUBSTATE_SRC})

target_link_libraries( # Specifies the target library.
        native-lib
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua_mod
        Utils
        )


add_library(pubg
        SHARED
        pubg.cpp
#        utils/Util.cpp
        ${SUBSTATE_SRC})
target_link_libraries( # Specifies the target library.
        pubg
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua_mod
        Utils
        )
add_library(ilongyuan
        SHARED
        longyuan.cpp
        #utils/Util.cpp
        ${SUBSTATE_SRC})
target_link_libraries( # Specifies the target library.
        ilongyuan
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua_mod
        Utils
        )


#add_library(cmb SHARED
#        cmb/cmb.cpp)
target_compile_definitions(${PROJECT_NAME} PRIVATE CMAKE_EXPORT_COMPILE_COMMANDS=1)
#target_include_directories(${PROJECT_NAME} PRIVATE )
#GameProtector3
add_executable(GameProtector3 ${PROJECT_SOURCE_DIR}/GameProtector3/main.cpp)
add_executable(inside ${PROJECT_SOURCE_DIR}/linux_inside/execve.c)
add_executable(waitpid_t ${PROJECT_SOURCE_DIR}/linux_inside/wait_pid.c)
#test goal
add_executable(fuckyu ${PROJECT_SOURCE_DIR}/exe/main.cpp)
add_executable(myecho ${PROJECT_SOURCE_DIR}/myecho/myecho.c)

#check attributes
message(STATUS "构建版本:${CMAKE_BUILD_TYPE}")
message(STATUS "源码:${SRC_LIST}\n ,${CMAKE_CXX_FLAGS} ")
