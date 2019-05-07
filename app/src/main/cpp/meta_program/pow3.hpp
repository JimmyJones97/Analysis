//
// Created by pwd61 on 2019/4/26.
//

#ifndef ANALYSIS_POW3_HPP
#define ANALYSIS_POW3_HPP

template<int N>
class Pow3 {
public:
    enum {
        result = 3 * Pow3<N - 1>::result
    };
};

template<>
class Pow3<0> {
public:
    enum {
        result = 1
    };
};

#endif //ANALYSIS_POW3_HPP
