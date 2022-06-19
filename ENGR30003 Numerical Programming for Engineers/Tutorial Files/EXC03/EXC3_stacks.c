#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define INIT_SIZE 5

/* data type */
typedef struct {
    int r;
    int g;
    int b;
} data_t;

data_t new_rgb(int r, int g, int b)
{
    data_t d;
    d.r = r;
    d.g = g;
    d.b = b;
    return d;
}

/* stack type */
typedef struct {
    int array_size;
    data_t* array;
    int top;
} stack_t;

/* create a new stack structure */
stack_t* stack_new()
{
    stack_t* s;
    s = (stack_t*)malloc(sizeof(stack_t));
    assert(s != NULL);
    s->array = (data_t*)malloc(INIT_SIZE * sizeof(data_t));
    assert(s != NULL);
    s->array_size = INIT_SIZE;
    s->top = -1;
    return s;
}

/* free all memory associated with a stack */
void stack_free(stack_t* s)
{
    assert(s != NULL);
    assert(s->array != NULL);
    free(s->array);
    free(s);
}

/* remove item on the stack */
data_t stack_pop(stack_t* s)
{
    assert(s != NULL);
    assert(s->array != NULL);
    assert(s->top >= 0);
    s->top--;
    return s->array[s->top + 1];
}

/* add item to the stack */
void stack_push(stack_t* s, data_t d)
{
    assert(s != NULL);
    assert(s->array != NULL);
    if (s->top == s->array_size - 1) {
        // need to make the array bigger
        s->array = (data_t*)realloc(s->array, s->array_size * 2 * sizeof(data_t));
        assert(s->array != NULL);
        s->array_size *= 2;
    }
    s->top++;
    s->array[s->top] = d;
}

int stack_empty(stack_t* s)
{
    return s->top == -1;
}

int stack_size(stack_t* s)
{
    return s->top + 1;
}

int main(int argc, char const* argv[])
{
    stack_t* s = stack_new();

    for (int i = 0; i < 5; i++) {
        data_t d = new_rgb(rand() % 255, rand() % 255, rand() % 255);
        stack_push(s, d);
        if (rand() % 3 == 0) {
            stack_pop(s);
       }
    }

    printf("%d items on the stack\n", stack_size(s));

    while (!stack_empty(s)) {
        data_t m = stack_pop(s);
    }

    printf("%d items on the stack\n", stack_size(s));

    stack_free(s);

    return 0;
}
