cmake_minimum_required (VERSION 3.4)

# DONT FORGET TO SET LD_LIBRARY_PATH = ${TARGET_NAME} LIBRARY TO ALLOW JAVA LOAD THE LIBRARY!!!
project(TransemaCodegen VERSION 0.0.1 DESCRIPTION
        "Codegeneration of Transema. It emits LLVM IR")
set(TARGET_NAME LLVMCodegen)

file(GLOB_RECURSE HEADERS ${CMAKE_CURRENT_SOURCE_DIR}/include/*.h)
file(GLOB_RECURSE SOURCES ${CMAKE_CURRENT_SOURCE_DIR}/lib/*.cpp)

find_package(JNI REQUIRED)
find_package(LLVM REQUIRED CONFIG)
set(LLVM_LIBS LLVMSupport LLVMIRReader LLVMBitWriter)

include_directories(${LLVM_INCLUDE_DIRS} ${JNI_INCLUDE_DIRS})

add_library(${TARGET_NAME} SHARED ${SOURCES} ${HEADERS})
target_include_directories(${TARGET_NAME} PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include)
target_link_libraries(${TARGET_NAME} ${LLVM_LIBS})
