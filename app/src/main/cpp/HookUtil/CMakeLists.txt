project(HookUtil C )


add_library(${PROJECT_NAME} SHARED  HookUtil.c)
add_executable(inj inject.c)

