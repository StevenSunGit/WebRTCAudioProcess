cmake_minimum_required(VERSION 3.4.1)
project(audio-effect)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2 -Wall -std=c++11 -fexceptions -fpermissive -fpic")

include_directories(include)

set(SOURCE_FILES
        src/audio_effect_engine_native.cpp)

add_library(${PROJECT_NAME} SHARED ${SOURCE_FILES})

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

target_link_libraries( # Specifies the target library.
        ${PROJECT_NAME}

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})