/* For this exercise you must read in a matrix and ouput the maximum and 
 * minimum values from each column. Use dynamic memory allocation to store
 * the matrix in memory and fill in steps 2-5 below
 *  
 * Chitrarth Lav, 27 July 2017 
 * modif. A. Kidanemariam 7 Aug 2020
 */

#include <stdio.h>
#include <stdlib.h>
//#include <stdint.h>
//#include <limits.h>


void* safe_malloc(size_t bytes)
{
    void* ptr = malloc(bytes);
    if (ptr == NULL) {
        printf("error mallocing %lu bytes\n", bytes);
        exit(EXIT_FAILURE);
    }
    return ptr;
}

int main(int argc, char const* argv[])
{
    int n, m;
    
    /* (1) read dimensions, 
       Input format:
       n = number of rows 
       m = number of cols 
    */
    if (scanf("n = %d\n", &n) != 1) {
        printf("error reading n dimension.\n");
        exit(EXIT_FAILURE);
    }
    if (scanf("m = %d\n", &m) != 1) {
        printf("error reading m dimension.\n");
        exit(EXIT_FAILURE);
    }
        
    /* (2) allocate memory */    
    int** matrix = (int**) safe_malloc(n * sizeof(int*));
    for (int i = 0; i < n; i++) 
        matrix[i] = (int*) safe_malloc(m * sizeof(int));
        
    // (3) read in the data
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (scanf("%d", &(matrix[i][j])) != 1) {
                printf("error reading matrix data.\n");
                exit(EXIT_FAILURE);
            }
        }
    }

    // (4) compute min/max of each column
    for (int i = 0; i < m; i++) {

        int min = matrix[0][i];
        int max = matrix[0][i];
        
        for (int j = 0; j < n; j++) {
            if (min > matrix[j][i])
                min = matrix[j][i];
            
            if (max < matrix[j][i])
                max = matrix[j][i];
        }
        printf("col %d: min = %d max = %d\n", i, min, max);
    }
    
    // (5) cleanup
    for (int i = 0; i < n; i++) 
        free(matrix[i]);
    
    free(matrix);

    return EXIT_SUCCESS;
}
