cmake_minimum_required (VERSION 3.4)

project(TransemaBackend VERSION 0.0.1 DESCRIPTION
        "Backend of Transema. Codegeneration, it emits LLVM IR")

set(TRANSEMA_SOURCE_DIR ${CMAKE_SOURCE_DIR})
set(TRANSEMA_BINARY_DIR ${CMAKE_BINARY_DIR})
set(TRANSEMA_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/include)

find_package(LLVM REQUIRED CONFIG)

# add_subdirectory(external)
add_subdirectory(lib)
add_subdirectory(tools)
