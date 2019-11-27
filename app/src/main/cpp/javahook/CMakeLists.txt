project(injectMain C ASM)
set(SRC_LISTS injectMain.c libinject.c shellcode.s)
#### shellcode注入方式
add_executable(${PROJECT_NAME} ${SRC_LISTS})
target_link_libraries(${PROJECT_NAME} log)
target_include_directories(${PROJECT_NAME} PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include/)



##### devik hook test






#### 汇编编译
add_executable(asm_ex shellcode_ex.s)
#target_link_options(asm_ex PRIVATE  "-nostdlib++")
set_target_properties(asm_ex PROPERTIES LINK_FLAGS "-nostdlib")

#target_compile_options( -nostdlib)

