cmake_minimum_required(VERSION 3.4.1)
project(native_lib)

#set(LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/out/library)
#set(EXECUTABLE_OUTPUT_PATH  ${PROJECT_SOURCE_DIR}/out/build)



#子目录
add_subdirectory(slua)
add_subdirectory(debugger)
add_subdirectory(memdump)
add_subdirectory(meta_program)
add_subdirectory(cmb)
add_subdirectory(utils)
add_subdirectory(Substrate)
add_subdirectory(SoFixer)
add_subdirectory(libxhook)
add_subdirectory(xhook_test)
#add_subdirectory(HackLib)
#add_subdirectory(zlib123)
#add_subdirectory(minizip)
add_subdirectory(linenoise)
add_subdirectory(whale)
add_subdirectory(signal_action_merchant)
add_subdirectory(memory_mmap)
add_subdirectory(ptrace_tool)
add_subdirectory(bintarget)
add_subdirectory(javahook)
add_subdirectory(helloworld)
add_subdirectory(HookUtil)
#add_subdirectory(CVE_2019_13272)
#add_subdirectory(chroot_cve)
#add_subdirectory(fileless_so)
#add_subdirectory(curl)
#add_subdirectory(openssl)
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
#find_library(cutils-lib
#        cutils
#        )
add_library( # Sets the name of the library.
        native_lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        jd/native-lib.cpp
        jd/jd_utils.cpp

        #utils/Util.cpp
        ${SUBSTATE_SRC})

target_link_libraries( # Specifies the target library.
        native_lib
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua
        Utils
        )

add_library(pubg
        SHARED
        pubg/pubg.cpp
#        utils/Util.cpp
        ${SUBSTATE_SRC})
target_link_libraries( # Specifies the target library.
        pubg
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua
        Utils
        )
add_library(ilongyuan
        SHARED
        wxww/longyuan.cpp
        #utils/Util.cpp
        #${SUBSTATE_SRC}
        )
target_link_libraries( # Specifies the target library.
        ilongyuan
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        slua
        Utils
        Substrate
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
add_executable(do_execve do_execve.c)
add_executable(show_info show_info.c)
add_executable(strsto linux_run/strtok_ex.c)
#check attributes
message(STATUS "构建版本:${CMAKE_BUILD_TYPE}")
message(STATUS "源码:${SRC_LIST}\n ,${CMAKE_CXX_FLAGS} ")
function(do_test msg)
    message(WARNING ${msg})
endfunction(do_test)

do_test("fuck ")
do_test(" thanks god")
add_executable(anyHook anyHook/RootUtils.cpp)
target_link_libraries(anyHook
        log
        z
        m)

add_library(mydlopen mydlopen/mydlopen.cpp)
target_include_directories(mydlopen PRIVATE
        $<BUILD_INTERFACE:${PROJECT_SOURCE_DIR}/mydlopen>)
add_library(sss SHARED yeecall/yeecall.cpp)
target_include_directories(sss
        PRIVATE
        $<BUILD_INTERFACE:${PROJECT_SOURCE_DIR}/yeecall/>)
target_link_libraries(sss Utils ${log-lib})

add_library(pltfun SHARED plt_hook_target.cpp)
target_include_directories(pltfun PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/)
add_executable(plt_hook_target   plt_main.cpp)
target_include_directories(plt_hook_target  PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/)
target_link_libraries(plt_hook_target PUBLIC pltfun)
#add_executable(plt_hook_ninja plt_hook_ninja.cpp)
#target_link_libraries(plt_hook_ninja Utils ${log-lib})
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../build)