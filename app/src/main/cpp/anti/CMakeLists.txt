project(anti)
add_library(${PROJECT_NAME} SHARED anti.c)
target_link_libraries(${PROJECT_NAME}  PRIVATE log)