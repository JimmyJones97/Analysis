project(cve_2019_13272)

file(GLOB SRC_SUB ${CMAKE_CURRENT_SOURCE_DIR}/*.c)

#add_library(${PROJECT_NAME} STATIC ${SRC_SUB})
#add_library(${PROJECT_NAME}  SHARED  ${SRC_SUB})
add_executable(exp ${SRC_SUB})
#target_link_libraries(${})

target_include_directories(exp
        INTERFACE
        $<BUILD_INTERFACE:${PROJECT_SOURCE_DIR}/>
        $<INSTALL_INTERFACE:include>)