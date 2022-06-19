#include <stdio.h>
#include <stdlib.h>
#include <assert.h>


/* Linked List examples */
/* modif. Aman Kidanemariam Aug 7, 2020 */
    
/* data type */
typedef struct {
    int r;
    int g;
    int b;
} rgb_t;

/* node type */
typedef struct node node_t;

struct node {
    void* data;
    node_t* next;
};

/* linked list type */
typedef struct {
    int num_elements;
    node_t* head;
    node_t* tail;
    void (*del)(void*);
} list_t;

/* print the RGB tuple */
void rgb_print(void* d)
{
    assert(d != NULL);
    rgb_t* rd = (rgb_t*)d;
    printf("(%.3d,%.3d,%.3d)\n", rd->r, rd->g, rd->b);
}

/* process all elements in the list */
void list_process_all(node_t* p, void (*process)(void*))
{
    while (p) {
        process(p->data);
        p = p->next;
    }
}

/* remove node at the front of the list */
void* list_pop_front(list_t* list)
{
    assert(list != NULL);
    assert(list->num_elements > 0);
    node_t* old;
    assert(list->head != NULL);
    old = list->head;
    list->head = list->head->next;
    void* d = old->data;
    free(old);
    list->num_elements--;
    if (list->num_elements == 0) {
        list->head = NULL;
        list->tail = NULL;
    }
    return d;
}

/* add node at the front of the list */
void list_push_front(list_t* list, void* d)
{
    assert(list != NULL);
    node_t* new = (node_t*)malloc(sizeof(node_t));
    assert(new);
    new->data = d;
    new->next = list->head;
    list->head = new;
    if (list->tail == NULL)
        list->tail = new;
    list->num_elements++;
}

/* add node add the back of the list */
void list_push_back(list_t* list, void* d)
{
    assert(list != NULL);
    node_t* new = (node_t*)malloc(sizeof(node_t));
    assert(new != NULL);
    new->data = d;
    new->next = NULL;
    if (list->tail)
        list->tail->next = new;
    list->tail = new;
    if (list->head == NULL)
        list->head = new;
    list->num_elements++;
}

/* TODO: implement a function inserts a new node (for item d)
         after node n
*/
void list_insert_after(list_t* list, node_t* n, void* d)
{
    assert(list != NULL);
    assert(n != NULL);
    assert(d != NULL);

    node_t* new = (node_t*)malloc(sizeof(node_t));
    assert(new != NULL);
    new->data = d;

    // link the new node
    new->next = n->next;
    n->next = new;

    // make sure the list pointers are up to date
    if (list->tail == n)
        list->tail = new;
    list->num_elements++;
}

/* TODO: implement a function which removes the
         last element in the list 
*/
void* list_pop_back(list_t* list)
{
    assert(list != NULL);
    assert(list->num_elements > 0);

    if (list->num_elements == 1){
        list_pop_front(list);
        return NULL;
    }
        
    node_t* old;
    old = list->tail;

    // Find the second last node in the list (start from head) 
    node_t* secondLast = list->head; 
    while (secondLast->next->next != NULL) 
        secondLast = secondLast->next;

    // make the second last node the last node in the list
    secondLast->next = NULL;
    list->tail = secondLast;

    // discard the old tail node
    free(old);

    // update the number of elements in the list
    list->num_elements--;
    if (list->num_elements == 0) {
        list->head = NULL;
        list->tail = NULL;
    }
    return NULL;
}

/* create a new linked list structure */
list_t* list_new(void (*delfunc)(void*))
{
    list_t* list;
    list = (list_t*)malloc(sizeof(list_t));
    assert(list != NULL);
    list->head = NULL;
    list->tail = NULL;
    list->num_elements = 0;
    list->del = delfunc;
    return list;
}

/* free all memory associated with a list */
void list_free(list_t* list)
{
    assert(list != NULL);
    while (list->num_elements) {
        void* elem = list_pop_front(list);
        list->del(elem);
    }
    free(list);
}

int main(int argc, char const* argv[])
{
    /* create new list object with regular 'free' as the del function */
    list_t* list = list_new(free);

    /* insert some elements at the back */
    assert(list->num_elements == 0);
    for (int i = 0; i < 5; i++) {
        rgb_t* new_dat = (rgb_t*)malloc(sizeof(rgb_t));
        new_dat->r = rand() % 255;
        new_dat->g = rand() % 255;
        new_dat->b = rand() % 255;
        list_push_back(list, new_dat);
    }
    assert(list->num_elements == 5);

    /* insert some elements at the front */
    for (int i = 0; i < 5; i++) {
        rgb_t* new_dat = (rgb_t*)malloc(sizeof(rgb_t));
        new_dat->r = rand() % 255;
        new_dat->g = rand() % 255;
        new_dat->b = rand() % 255;
        list_push_front(list, new_dat);
    }
    assert(list->num_elements == 10);

    /* process all elements in list */
    list_process_all(list->head, rgb_print);
    printf("\n\n");

    /* find a node in the middle */
    node_t* cur = list->head;
    for (size_t i = 0; i < 5; i++) {
        cur = cur->next;
    }
    /* create a new item */
    rgb_t* ndat = (rgb_t*)malloc(sizeof(rgb_t));
    ndat->r = 1;
    ndat->g = 1;
    ndat->b = 1;
    list_insert_after(list, cur, ndat);
    assert(list->num_elements == 11);

    /* process all elements in list */
    list_process_all(list->head, rgb_print);
    printf("\n\n");

    /* use new function to insert at tail */
    /* create a new item */
    rgb_t* ndat2 = (rgb_t*)malloc(sizeof(rgb_t));
    ndat2->r = 2;
    ndat2->g = 2;
    ndat2->b = 2;
    list_insert_after(list, list->tail, ndat2);
    assert(list->num_elements == 12);

    /* process all elements in list */
    list_process_all(list->head, rgb_print);

    /* remove the last element in the list*/
    list_pop_back(list);
    assert(list->num_elements == 11);
    
    printf("\n\n");
    list_process_all(list->head, rgb_print);
    
    /* remove everything */
    list_free(list);

    return 0;
}
