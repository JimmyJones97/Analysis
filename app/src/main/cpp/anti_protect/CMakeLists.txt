project(anti_pro)
add_library(${PROJECT_NAME} SHARED anti_protect.c)
target_link_libraries(${PROJECT_NAME}  PRIVATE log)