project(inject C ASM)

set(SRC_LISTS injectMain.c libinject.c shellcode.s)

add_executable(${PROJECT_NAME} ${SRC_LISTS})
target_link_libraries(${PROJECT_NAME} log)
target_include_directories(${PROJECT_NAME} PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include/)


set(LIB_SRC_LIST DalvikMethodHook.cpp libtest.cpp)
link_directories(${CMAKE_CURRENT_SOURCE_DIR}/lib)
add_library(testso SHARED ${LIB_SRC_LIST})
target_include_directories(testso PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include/)
#target_compile_definitions(testso  -I./jni/include/  )
add_definitions(-DDEBUG )
target_link_libraries(testso log dvm.4 android_runtime)

add_executable(asm_ex shellcode_ex.s)
#target_link_options(asm_ex PRIVATE  "-nostdlib++")
set_target_properties(asm_ex PROPERTIES LINK_FLAGS "-nostdlib")

#target_compile_options( -nostdlib)

