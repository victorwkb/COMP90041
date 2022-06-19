/***************************************************************************
 *
 *   File        : tasks.c
 *   Student Id  : 981436
 *   Name        : KAI BING GOH
 *
 ***************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <string.h>
#include "tasks.h"
#include <assert.h>

#define X_20 20.0
#define Y_ZERO 0.0
#define X_LO_BOUND -10.0
#define X_HI_BOUND 80.0
#define Y_LO_BOUND -18.0
#define Y_HI_BOUND 18.0
#define PERCENTAGE_75 0.75
#define ONE_HUNDRED 100.0
#define THRESHOLD_LO 0.0
#define THRESHOLD_HI 25.0
#define BST_SUCCESS 1
#define BST_FAILURE 0
#define S_TO_MS 1000.0
#define MS_TO_MICROS 1000.0
#define MAX_W 25.0
#define BUFFER_MULTIPLIER 2.0

typedef struct node node_t;

typedef struct node2 bst_node_t;

typedef struct
{
    double rho_or_S;
    double u;
    double v;
    double x;
    double y;
} data_t;

typedef struct
{
    int num_elements;
    node_t *head;
    node_t *tail;
    void (*del)(void *);
} list_t;

typedef struct
{
    int num_elements;
    bst_node_t *root;
    void (*del)(void *);
    int (*cmp)(void *, void *);
} bst_t;

struct node
{
    double x;
    double y;
    double u;
    double v;
    double rho;
    node_t *next;
};

struct node2
{
    void *data;
    bst_node_t *left;
    bst_node_t *right;
};

void *safe_malloc(size_t num_bytes)
{
    void *ptr = malloc(num_bytes);
    if (ptr == NULL)
    {
        printf("ERROR: malloc(%lu)\n", num_bytes);
        exit(EXIT_FAILURE);
    }
    return ptr;
}

void *safe_realloc(void *ptr, size_t num_bytes)
{
    void *new_ptr = realloc(ptr, num_bytes);
    if (new_ptr == NULL)
    {
        printf("ERROR: realloc(%lu)\n", num_bytes);
        exit(EXIT_FAILURE);
    }
    return new_ptr;
}

size_t file_size_in_bytes(FILE *fp)
{
    assert(fp != NULL);
    long cur_pos = ftell(fp);
    if (cur_pos == -1)
    {
        perror("ftell error.");
        exit(EXIT_FAILURE);
    }
    /* seek to the end */
    int ret = fseek(fp, 0L, SEEK_END);
    if (ret == -1)
    {
        perror("file seek error.");
        exit(EXIT_FAILURE);
    }
    long fs = ftell(fp);

    /* seek back to the previous position */
    ret = fseek(fp, cur_pos, SEEK_SET);
    if (ret == -1)
    {
        perror("file seek error.");
        exit(EXIT_FAILURE);
    }
    return (size_t)fs;
}

FILE *safe_fopen(const char *path, const char *mode)
{
    FILE *fp = fopen(path, mode);
    if (fp == NULL)
    {
        perror("file open error.");
        exit(EXIT_FAILURE);
    }
    return fp;
}

int file_exists(const char *path)
{
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        return 0;
    }
    fclose(fp);
    return 1;
}

int cmpfunc(const void *a, const void *b)
{
    const double *da = (const double *)a;
    const double *db = (const double *)b;

    return (*da > *db) - (*da < *db);
}

list_t *list_new(void (*delfunc)(void *))
{
    list_t *list;
    list = (list_t *)malloc(sizeof(list_t));
    assert(list != NULL);
    list->head = NULL;
    list->tail = NULL;
    list->num_elements = 0;
    list->del = delfunc;
    return list;
}

void list_push_back(list_t *list, double x, double y, double u, double v, double rho)
{
    assert(list != NULL);
    node_t *new = (node_t *)malloc(sizeof(node_t));
    assert(new != NULL);
    new->x = x;
    new->y = y;
    new->u = u;
    new->v = v;
    new->rho = rho;
    new->next = NULL;
    if (list->tail)
        list->tail->next = new;
    list->tail = new;
    if (list->head == NULL)
        list->head = new;
    list->num_elements++;
}

void list_push_front(list_t *list, double x, double y, double u, double v, double rho)
{
    assert(list != NULL);
    node_t *new = (node_t *)malloc(sizeof(node_t));
    assert(new);
    new->x = x;
    new->y = y;
    new->u = u;
    new->v = v;
    new->rho = rho;
    new->next = list->head;
    list->head = new;
    if (list->tail == NULL)
        list->tail = new;
    list->num_elements++;
}

void *list_pop_front(list_t *list)
{
    assert(list != NULL);
    assert(list->num_elements > 0);
    node_t *old;
    assert(list->head != NULL);
    old = list->head;
    list->head = list->head->next;
    free(old);
    list->num_elements--;
    if (list->num_elements == 0)
    {
        list->head = NULL;
        list->tail = NULL;
    }
    return 0;
}

void list_free(list_t *list)
{
    assert(list != NULL);
    while (list->num_elements)
    {
        void *elem = list_pop_front(list);
        list->del(elem);
    }
    free(list);
}

bst_t *bst_new(void (*delfunc)(void *), int (*cmpfunc)(void *, void *))
{
    bst_t *bst;
    bst = (bst_t *)malloc(sizeof(bst_t));
    assert(bst != NULL);
    bst->root = NULL;
    bst->num_elements = 0;
    bst->del = delfunc;
    bst->cmp = cmpfunc;
    return bst;
}

bst_node_t *bst_find(bst_t *bst, void *d)
{
    assert(bst != NULL);
    assert(d != NULL);
    bst_node_t *tmp = bst->root;
    while (tmp)
    {
        if (bst->cmp(tmp->data, d) > 0)
        { // element is smaller
            tmp = tmp->left;
        }
        else if (bst->cmp(tmp->data, d) < 0)
        { // element is bigger
            tmp = tmp->right;
        }
        else
        {
            break;
        }
    }
    return tmp;
}

int bst_cmp(void *a, void *b)
{
    if (*(double *)a < *(double *)b)
    {
        return -1;
    }
    else if (*(double *)a > *(double *)b)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

int bst_insert(bst_t *bst, void *d)
{
    assert(bst != NULL);
    assert(d != NULL);
    bst_node_t *parent = NULL;
    bst_node_t *tmp = bst->root;
    while (tmp)
    {
        parent = tmp;
        if (bst->cmp(tmp->data, d) > 0)
        { // element is smaller
            tmp = tmp->left;
        }
        else if (bst->cmp(tmp->data, d) < 0)
        { // element is bigger
            tmp = tmp->right;
        }
        else
        {
            return BST_FAILURE;
        }
    }
    bst_node_t *new_node = (bst_node_t *)malloc(sizeof(bst_node_t));
    assert(new_node != NULL);
    new_node->data = d;
    new_node->left = NULL;
    new_node->right = NULL;
    if (parent != NULL)
    {
        if (bst->cmp(parent->data, d) > 0)
        { // element is smaller
            assert(parent->left == NULL);
            parent->left = new_node;
        }
        else
        {
            assert(parent->right == NULL);
            parent->right = new_node;
        }
    }
    else
    {
        assert(bst->root == NULL);
        bst->root = new_node;
    }
    bst->num_elements++;

    return BST_SUCCESS;
}

void bst_free_subtree(bst_t *bst, bst_node_t *n)
{
    assert(bst != NULL);
    if (n)
    {
        bst_free_subtree(bst, n->left);
        bst_free_subtree(bst, n->right);
        bst->del(n->data);
        free(n);
        bst->num_elements--;
    }
}

void bst_free(bst_t *bst)
{
    assert(bst != NULL);
    bst_free_subtree(bst, bst->root);
    free(bst);
}

void bst_search(bst_t *bst, void *d)
{
    FILE *fpout = safe_fopen("task3.csv", "a");
    assert(bst != NULL);
    assert(d != NULL);
    bst_node_t *tmp = bst->root;
    while (tmp)
    {
        if (bst->cmp(tmp->data, d) > 0)
        { // element is smaller
            fprintf(fpout, "%.6lf", *(double *)tmp->data);
            tmp = tmp->left;
            if (tmp != NULL)
            {
                fprintf(fpout, ",");
            }
        }
        else if (bst->cmp(tmp->data, d) < 0)
        { // element is bigger
            fprintf(fpout, "%.6lf", *(double *)tmp->data);
            tmp = tmp->right;
            if (tmp != NULL)
            {
                fprintf(fpout, ",");
            }
        }
        else
        {
            fprintf(fpout, "%.6lf", *(double *)tmp->data);
            break;
        }
    }
}

void balanced_insert(bst_t *bst, double *d, int start, int end)
{
    // Base case
    if (end > start)
    {
        // Gets middle element and make it root
        int mid = (start + end) / 2;
        bst_insert(bst, &d[mid]);

        // Recurse
        balanced_insert(bst, d, start, mid - 1);
        balanced_insert(bst, d, mid + 1, end);
    }
}

void binary_search(double *rhov, int lo, int hi, double x, FILE *fp3)
{
    while (lo <= hi)
    {
        int m = (lo + hi) / 2;
        if (x < rhov[m])
        {
            hi = m - 1;
            fprintf(fp3, "%.6lf,", rhov[m]);
        }
        else if (x > rhov[m])
        {
            lo = m + 1;
            fprintf(fp3, "%.6lf,", rhov[m]);
        }
        else
        {
            fprintf(fp3, "%.6lf\n", rhov[m]);
            break;
        }
    }
}

/*TASK 1 MAIN FUNCTION*/
void max_min_flux(const char *flow_file)
{
    double tempx, tempy, tempu, tempv, temprho;
    // Making new linked list to assign variables into it
    list_t *list1 = list_new(free);
    assert(list1->num_elements == 0);
    FILE *fp = safe_fopen(flow_file, "r");
    // Get rids of the first line and scanning from the second line onwards
    while ((fgetc(fp)) != '\n')
    {
    }
    while (fscanf(fp, "%lf,%lf,%lf,%lf,%lf\n", &tempx, &tempy, &tempu, &tempv, &temprho) == 5)
    {
        list1->num_elements++;
        list_push_back(list1, tempx, tempy, tempu, tempv, temprho);
    }
    fclose(fp);
    // Creating "task1.csv" file for output
    FILE *fp2 = safe_fopen("task1.csv", "w");
    fprintf(fp2, "x,y,u,v,rho\n");
    // Node_t pointers to retain rhov and rhou's location to access it later for output
    node_t *p = list1->head;
    node_t *tmp1 = p;
    node_t *tmp2 = p;
    node_t *tmp3 = p;
    node_t *tmp4 = p;
    // Comparing nodes by nodes through the list for max/min values
    double rhou = 0, rhoumax = 0, rhoumin = 0, rhov = 0, rhovmax = 0, rhovmin;
    while (p != NULL)
    {
        if (p->x > X_20)
        {
            rhou = (p->u) * (p->rho);
            rhoumax = (tmp1->u) * (tmp1->rho);
            if (rhou > rhoumax)
            {
                tmp1 = p;
            }
            rhoumin = (tmp2->u) * (tmp2->rho);
            if (rhou < rhoumin)
            {
                tmp2 = p;
            }
            rhov = (p->v) * (p->rho);
            rhovmax = (tmp3->v) * (tmp3->rho);
            if (rhov > rhovmax)
            {
                tmp3 = p;
            }
            rhovmin = (tmp4->v) * (tmp4->rho);
            if (rhov < rhovmin)
            {
                tmp4 = p;
            }
        }
        p = p->next;
    }
    // fprintf output according to the format
    fprintf(fp2, "%.6lf,%.6lf,%.6lf,%.6lf,%.6lf\n", tmp1->x, tmp1->y, tmp1->u, tmp1->v, tmp1->rho);
    fprintf(fp2, "%.6lf,%.6lf,%.6lf,%.6lf,%.6lf\n", tmp2->x, tmp2->y, tmp2->u, tmp1->v, tmp2->rho);
    fprintf(fp2, "%.6lf,%.6lf,%.6lf,%.6lf,%.6lf\n", tmp3->x, tmp3->y, tmp3->u, tmp1->v, tmp3->rho);
    fprintf(fp2, "%.6lf,%.6lf,%.6lf,%.6lf,%.6lf", tmp4->x, tmp4->y, tmp4->u, tmp1->v, tmp4->rho);
    fclose(fp2);
    // list_free(list1);
}

/*TASK 2 MAIN FUNCTION*/
void coarsegrid(const char *flow_file, int grid_resolution)
{
    // Made new linked list to contain the information
    list_t *list1 = list_new(free);
    assert(list1->num_elements == 0);
    FILE *fp = safe_fopen(flow_file, "r");
    double tempx, tempy, tempu, tempv, temprho;
    while ((fgetc(fp)) != '\n')
    {
    }
    while (fscanf(fp, "%lf,%lf,%lf,%lf,%lf\n", &tempx, &tempy, &tempu, &tempv, &temprho) == 5)
    {
        if (tempx > X_LO_BOUND && tempx < X_HI_BOUND && tempy > Y_LO_BOUND && tempy < Y_HI_BOUND)
        {
            list1->num_elements++;
            list_push_back(list1, tempx, tempy, tempu, tempv, temprho);
        }
    }
    fclose(fp);
    // Calculate width and height for each cell and declaring 2d arrays for storing information
    double gridxwidth = (double)((X_HI_BOUND - X_LO_BOUND) / (double)grid_resolution);
    double gridyheight = (double)((Y_HI_BOUND - Y_LO_BOUND) / (double)grid_resolution);
    double tempxl = X_LO_BOUND, tempxu = X_LO_BOUND, tempyl = Y_LO_BOUND, tempyu = X_LO_BOUND;
    double qx[grid_resolution][grid_resolution], qy[grid_resolution][grid_resolution],
        qu[grid_resolution][grid_resolution], qv[grid_resolution][grid_resolution],
        S[grid_resolution][grid_resolution], x, y, u, v;
    // Sets upper and lower boundaries for each cells, starting from the bottom left
    for (int i = 0; i < grid_resolution; i++)
    {
        // Checking pt falling within each row, and within each row check for each column
        tempxu += gridxwidth;
        tempyl = Y_LO_BOUND;
        tempyu = Y_LO_BOUND;
        for (int j = 0; j < grid_resolution; j++)
        {
            x = y = u = v = 0.0;
            int c = 0;
            tempyu += gridyheight;
            // Searching for pt through list that satisfies the cells' boundary conditions
            node_t *p = list1->head;
            while (p != NULL)
            {
                // Excluding centrelines (cell boundary lines)
                if ((p->x > tempxl) && (p->x < tempxu) && (p->y > tempyl) && (p->y < tempyu))
                {
                    x += p->x;
                    y += p->y;
                    u += p->u;
                    v += p->v;
                    c++;
                }
                p = p->next;
            }
            if (c != 0)
            {
                // Compute averages
                qx[i][j] = (double)(x / c);
                qy[i][j] = (double)(y / c);
                qu[i][j] = (double)(u / c);
                qv[i][j] = (double)(v / c);
                S[i][j] = ONE_HUNDRED * sqrt(pow(qu[i][j], 2) + pow(qv[i][j], 2)) /
                          sqrt(pow(qx[i][j], 2) + pow(qy[i][j], 2));
            }
            else
            {
                qx[i][j] = qy[i][j] = qu[i][j] = qv[i][j] = S[i][j] = 0;
            }
            tempyl = tempyu;
        }
        tempxl = tempxu;
        // Update lower boundaries at the end for both x/y everytime it loops
    }
    // Put information into an empty linked list as the unsorted linked list
    list_t *unsorted = list_new(free);
    assert(unsorted->num_elements == 0);
    for (int i = 0; i < grid_resolution; i++)
    {
        for (int j = 0; j < grid_resolution; j++)
        {
            list_push_back(unsorted, qx[i][j], qy[i][j], qu[i][j], qv[i][j], S[i][j]);
        }
    }
    // Algorithm to sort linked list (Finding max, taking values smaller to the most recently added
    // values in the list but greatest in the unsorted list excluding taken values)

    node_t *p2 = unsorted->head;
    FILE *fp2 = safe_fopen("task2.csv", "w");
    fprintf(fp2, "S,x,y,u,v");
    list_t *sorted = list_new(free);
    assert(sorted->num_elements == 0);

    node_t *p4 = unsorted->head;
    node_t *tmp1 = p4;
    node_t *min_p = unsorted->head;
    node_t *min_loc = NULL;
    double max;
    double min = min_p->rho;

    while (min_p != NULL)
    {
        if (min_p->rho < min)
        {
            min = min_p->rho;
            min_loc = min_p;
        }
        min_p = min_p->next;
    }
    max = min;
    if (sorted->head == NULL)
    {
        while (p4 != NULL)
        {
            if (p4->rho > max)
            {
                max = p4->rho;
                tmp1 = p4;
            }
            p4 = p4->next;
        }
        list_push_back(sorted, tmp1->x, tmp1->y, tmp1->u, tmp1->v, tmp1->rho);
        tmp1 = NULL;
    }
    for (size_t i = 0; i < (unsorted->num_elements - 1); i++)
    {
        node_t *order = sorted->tail;
        max = min;
        while (p2 != NULL)
        {
            if (p2->rho > max && p2->rho < order->rho && p2->rho != min)
            {
                max = p2->rho;
                tmp1 = p2;
            }
            p2 = p2->next;
        }
        p2 = unsorted->head;
        if (tmp1->rho != sorted->tail->rho)
        {
            list_push_back(sorted, tmp1->x, tmp1->y, tmp1->u, tmp1->v, tmp1->rho);
        }
        if (i == unsorted->num_elements - 2)
        {
            list_push_back(sorted, min_loc->x, min_loc->y, min_loc->u, min_loc->v, min_loc->rho);
        }
    }
    node_t *psorted = sorted->head;
    while (psorted != NULL)
    {
        fprintf(fp2, "\n%.6lf,%.6lf,%.6lf,%.6lf,%.6lf", psorted->rho, psorted->x, psorted->y, psorted->u, psorted->v);
        psorted = psorted->next;
    }
    // list_free(unsorted);
    // list_free(sorted);
}

/*TASK 3 MAIN FUNCTION*/
void searching(const char *flow_file)
{
    // First read to find number of lines(nodes) in data
    double tempx, tempy, tempu, tempv, temprho;
    int num_lines = 0, i = 0;
    FILE *fp = safe_fopen(flow_file, "r");
    while (fgetc(fp) != '\n')
    {
    }
    while (fscanf(fp, "%lf,%lf,%lf,%lf,%lf\n", &tempx, &tempy, &tempu, &tempv, &temprho) == 5)
    {
        if (tempy == 0.0)
        {
            num_lines++;
        }
    }
    rewind(fp);
    double rhov[num_lines];
    // Declare an array for rho*v values, second read to assign the values into the array using temporary variables
    while (fgetc(fp) != '\n')
    {
    }
    while (fscanf(fp, "%lf,%lf,%lf,%lf,%lf\n", &tempx, &tempy, &tempu, &tempv, &temprho) == 5)
    {
        if (tempy == Y_ZERO)
        {
            rhov[i] = temprho * tempv;
            i++;
        }
    }
    fclose(fp);
    // Prepare output file for different lines
    FILE *fp2 = safe_fopen("task3.csv", "w");
    // Sorting array using QuickSort
    qsort(rhov, num_lines, sizeof(double), cmpfunc);
    // Calculate 75% of max value
    double max = rhov[num_lines - 1], max75 = PERCENTAGE_75 * max;
    struct timeval start, stop;

    // Linear search to find the closest value to 75% of the max value
    double min_dif = fabs(rhov[0] - max75);
    int loc = 0;
    // Taking time taken for linear search to be completed while printing outputs for line 1
    gettimeofday(&start, NULL);
    for (i = 0; i < num_lines; i++)
    {
        if (fabs(rhov[i] - max75) < min_dif)
        {
            min_dif = fabs(rhov[i] - max75);
            loc = i;
        }
        if (i != 0 && i == loc)
        {
            fprintf(fp2, ",");
        }
        if (i <= loc)
        {
            fprintf(fp2, "%.6lf", rhov[i]);
        }
    }
    fprintf(fp2, "\n");
    gettimeofday(&stop, NULL);
    double elapsed_ms = (stop.tv_sec - start.tv_sec) * S_TO_MS;
    elapsed_ms += (stop.tv_usec - start.tv_usec) / S_TO_MS;
    printf("TASK 3 Array Linear Search:  %.2lf microseconds\n", elapsed_ms * MS_TO_MICROS);

    // Array binary search for line 2 of output using target value in the array
    int lo = 0, hi = num_lines - 1;
    // Taking time taken for binary search to be completed while printing outputs for line 2
    gettimeofday(&start, NULL);
    binary_search(rhov, lo, hi, rhov[loc], fp2);
    gettimeofday(&stop, NULL);
    elapsed_ms = (stop.tv_sec - start.tv_sec) * S_TO_MS;
    elapsed_ms += (stop.tv_usec - start.tv_usec) / S_TO_MS;
    printf("TASK 3 Array Binary Search:  %.2lf microseconds\n", elapsed_ms * MS_TO_MICROS);

    // Creating linked list and scanning values into nodes while reusing the same nodes from previous tasks)
    list_t *list1 = list_new(free);
    assert(list1->num_elements == 0);
    for (i = 0; i < num_lines; i++)
    {
        list_push_back(list1, 0, 0, 0, 0, rhov[i]);
    }

    // Linear search through the linked list and take the time taken for task completion
    node_t *p = list1->head;
    int n = 0;
    gettimeofday(&start, NULL);
    while (p != NULL)
    {
        if (p->rho <= rhov[loc])
        {
            fprintf(fp2, "%.6lf", p->rho);
        }
        if (n < loc)
        {
            fprintf(fp2, ",");
        }
        p = p->next;
        n++;
    }
    fprintf(fp2, "\n");
    gettimeofday(&stop, NULL);
    elapsed_ms = (stop.tv_sec - start.tv_sec) * S_TO_MS;
    elapsed_ms += (stop.tv_usec - start.tv_usec) / S_TO_MS;
    printf("TASK 3 List Linear Search:  %.2f microseconds\n", elapsed_ms * MS_TO_MICROS);

    // Creating self-balancing BST for fastest runtime
    bst_t *bst = bst_new(free, bst_cmp);
    balanced_insert(bst, rhov, 0, num_lines - 1);
    // Taking down time taken for the algorithm to run and call the function
    gettimeofday(&start, NULL);
    bst_search(bst, (void *)&rhov[loc]);
    gettimeofday(&stop, NULL);
    elapsed_ms = (stop.tv_sec - start.tv_sec) * S_TO_MS;
    elapsed_ms += (stop.tv_usec - start.tv_usec) / S_TO_MS;
    printf("TASK 3 BST Search:  %.2f microseconds\n", elapsed_ms * MS_TO_MICROS);
    //All time taken for different algorithms to be completed are printed out in the terminal
    fclose(fp2);
    // list_free(list1);
    // bst_free(bst);
}

/*TASK 4 MAIN FUNCTION*/
void calculate_vorticity(const char *flow_file, int vorticity_numthreshold)
{
    int n_count = 0, m_count = 0, n_lines = 0, buff = 1;
    double x, y, u, v, rho, x_prev = 0.0, y_prev = 0.0;
    FILE *fp = safe_fopen(flow_file, "r");
    while ((fgetc(fp)) != '\n')
    {
    }
    data_t *array = safe_malloc(sizeof(data_t));
    while (fscanf(fp, "%lf,%lf,%lf,%lf,%lf\n", &x, &y, &u, &v, &rho) == 5)
    {
        if (m_count == 0)
        {
            x_prev = x;
        }
        if (x_prev == x)
        {
            x = x_prev;
            m_count++;
        }
        if (n_count == 0)
        {
            y_prev = y;
        }
        if (y_prev == y)
        {
            y = y_prev;
            n_count++;
        }
        if (n_lines == buff - 1)
        {
            buff = buff * BUFFER_MULTIPLIER;
            array = safe_realloc(array, buff * sizeof(data_t));
        }
        array[n_lines].x = x;
        array[n_lines].y = y;
        array[n_lines].u = u;
        array[n_lines].v = v;
        n_lines++;
    }

    data_t **array2by2 = (data_t **)safe_malloc(n_count * m_count * sizeof(data_t));
    double **w = (double **)safe_malloc(n_count * sizeof(double));
    int n = n_count, m = m_count;
    // Putting into 2-D Array
    for (int i = 0; i < n; i++)
    {
        array2by2[i] = (data_t *)safe_malloc(m * sizeof(data_t));
        w[i] = (double *)safe_malloc(m * sizeof(double));
    }
    for (size_t c = 0, j = 0; j < m; ++j)
    {
        for (size_t i = 0; i < n; ++i)
        {
            array2by2[i][j] = array[c++];
        }
    }

    // Computing the vorticity according to conditions given
    for (size_t i = 0; i < n - 2; i++)
    {
        for (size_t j = 0; j < m - 2; j++)
        {
            w[i][j] = (array2by2[i + 1][j].v - array2by2[i][j].v) / (array2by2[i + 1][j].x - array2by2[i][j].x) -
                      (array2by2[i][j + 1].u - array2by2[i][j].u) / (array2by2[i][j + 1].y - array2by2[i][j].y);
        }
    }

    for (size_t j = 0; j < m - 2; j++)
    {
        w[n - 1][j] = (array2by2[n - 1][j].v - array2by2[n - 2][j].v) / (array2by2[n - 1][j].x - array2by2[n - 2][j].x) -
                      (array2by2[n - 1][j + 1].u - array2by2[n - 1][j].u) / (array2by2[n - 1][j + 1].y - array2by2[n - 1][j].y);
    }

    for (size_t i = 0; i < n - 2; i++)
    {
        w[i][m - 1] = (array2by2[i + 1][m - 1].v - array2by2[i][m - 1].v) / (array2by2[i + 1][m - 1].x - array2by2[i][m - 1].x) -
                      (array2by2[i][m - 1].u - array2by2[i][m - 2].u) / (array2by2[i][m - 1].y - array2by2[i][m - 2].y);
    }

    w[n - 1][m - 1] = (array2by2[n - 1][m - 1].v - array2by2[n - 2][m - 1].v) / (array2by2[n - 1][m - 1].x - array2by2[n - 2][m - 1].x) -
                      (array2by2[n - 1][m - 1].u - array2by2[n - 1][m - 2].u) / (array2by2[n - 1][m - 1].y - array2by2[n - 1][m - 2].y);

    // Initialising pt array that depends on the argv input threshold number
    int pt[vorticity_numthreshold];
    for (size_t c = 0; c < vorticity_numthreshold; c++)
    {
        pt[c] = 0;
    }

    // Calculating size in between thresholds for setting up upper and lower boundaries for each thresholds
    double size_threshold = MAX_W / (vorticity_numthreshold - 1), threshold_border[vorticity_numthreshold];

    for (size_t c = 0; c < vorticity_numthreshold; c++)
    {
        threshold_border[c] = size_threshold * c;
    }

    // Counting number of pt that exists within each edges
    for (size_t c = 0; c < vorticity_numthreshold; c++)
    {
        for (size_t j = 0; j < m; j++)
        {
            for (size_t i = 0; i < n; i++)
            {
                if (c == vorticity_numthreshold - 1)
                {
                    if (fabs(w[i][j]) >= threshold_border[c])
                    {
                        pt[c]++;
                    }
                }
                else
                {
                    if (fabs(w[i][j]) >= threshold_border[c] && fabs(w[i][j]) < threshold_border[c + 1])
                    {
                        pt[c]++;
                    }
                }
            }
        }
    }

    FILE *fpath = safe_fopen("task4.csv", "w");
    fprintf(fpath, "threshold,pt");
    for (size_t c = 0; c < vorticity_numthreshold; c++)
    {
        fprintf(fpath, "\n%.6lf,%d", threshold_border[c], pt[c]);
    }
    fclose(fpath);
}