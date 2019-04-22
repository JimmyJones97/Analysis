//
// Created by pwd61 on 2019/4/3.
//
#include<iostream>
#include <pthread.h>
#include <unistd.h>

using namespace std;
enum index {
    underflow, overflow
};

void *child1(void *arg);

void *child2(void *arg);

int array_index(int *A, int n, int index);

pthread_key_t key;
struct test_struct {
    int i;
    float k;
};

int main() {
    int *A = new int[10];
    for (int i = 0; i < 10; i++)
        A[i] = i;
    try {
        cout << array_index(A, 10, 5) << endl;
        cout << array_index(A, 10, -1) << endl;
        cout << array_index(A, 10, 15) << endl;

    }
    catch (index e) {
        if (e == underflow) {
            cout << "index underflow!" << endl;
            exit(-1);
        }
        if (e == overflow) {
            cout << "index overflow!" << endl;
            exit(-1);
        }
    }

    pthread_t tid1, tid2;
    pthread_key_create(&key, nullptr);
    pthread_create(&tid1, nullptr, child1, nullptr);
    pthread_create(&tid2, nullptr, child2, nullptr);
    pthread_join(tid1, nullptr);
    pthread_join(tid2, nullptr);
    pthread_key_delete(key);

    return 0;
}

int array_index(int *A, int n, int index) {
    if (index < 0) throw underflow;
    if (index > n - 1) throw overflow;
    return A[index];
}

void *child1(void *arg) {
    struct test_struct struct_data = {};
    struct_data.i = 10;
    struct_data.k = 3.1415;
    pthread_setspecific(key, &struct_data);
    printf("结构体struct_data的地址为 0x%p\n", &(struct_data));
    printf("child1 中 pthread_getspecific(key)返回的指针为:0x%p\n",
           (struct test_struct *) pthread_getspecific(key));
    printf("利用 pthread_getspecific(key)打印 child1 线程中与key关联的结构体中成员值:\nstruct_data.i:%d\nstruct_data.k: %f\n",
           ((struct test_struct *) pthread_getspecific(key))->i,
           ((struct test_struct *) pthread_getspecific(key))->k);
    printf("------------------------------------------------------\n");
}

void *child2(void *arg) {
    int temp = 20;
    sleep(2);
    printf("child2 中变量 temp 的地址为 0x%p\n", &temp);
    pthread_setspecific(key, &temp);
    printf("child2 中 pthread_getspecific(key)返回的指针为:0x%p\n", (int *) pthread_getspecific(key));
    printf("利用 pthread_getspecific(key)打印 child2 线程中与key关联的整型变量temp 值:%d\n",
           *((int *) pthread_getspecific(key)));
}