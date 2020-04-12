

#include <cstdlib>
#include <iostream>
struct Mammal {
    Mammal() { std::cout << "Mammal::Mammal\n"; }
    virtual ~Mammal() { std::cout << "Mammal::~Mammal\n"; };
    virtual void run() = 0;
    virtual void walk() = 0;
    virtual void move() { walk(); }
};
struct Cat : Mammal {
    Cat() { std::cout << "Cat::Cat\n"; }
    virtual ~Cat() { std::cout << "Cat::~Cat\n"; }
    virtual void run() { std::cout << "Cat::run\n"; }
    virtual void walk() { std::cout << "Cat::walk\n"; }
};
struct Dog : Mammal {
    Dog() { std::cout << "Dog::Dog\n"; }
    virtual ~Dog() { std::cout << "Dog::~Dog\n"; }
    virtual void run() { std::cout << "Dog::run\n"; }
    virtual void walk() { std::cout << "Dog::walk\n"; }
};

int main(){
    Mammal *m;
    if (rand() % 2) {
        m = new Cat();
    } else {
        m = new Dog();
    }
    m->walk();
    delete m;
    return 0;
}