/*********************************************************************** 
 *
 * Program written by  Aman Kidanemariam 25/09/2020
 *
 * Solve Least Squares Polynomial Approximation of degree n
 *
 * Usage: suppose we want to fit 5th order polynomial to the x,y data  
 *        stored in file input_file.csv 
 *
 *        ./exec input_file.csv 5
 *
 *        Note: data is stored in input_file.csv in the following format:
 *              x,y
 *              1.0,3.0
 *              2.0,4,0
 *              .
 *              .
 *              .
 **************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>

#define SMALL 1e-14

typedef struct {
    double x;
    double y;
}data_t;

void solveLU      (double** const A,   double* X, double* const C, int n);
void decomposeLU  (double** const A,   double** L, double** U, int n);
void solveX       (double** const U,   double* const R, double* X, int n);
void solveR       (double** const L,   double* const C, double* R, int n);

/*********************************************************************/
int main(int argc, char *argv[])
{
    char* fName = argv[1];
    int polyDegree = atoi(argv[2]);


    int dataRead = 0;
    int buf_size = 1;
    data_t *data = (data_t*) malloc(buf_size*sizeof(data_t));
    data_t *ptr = data;
    
    /* Read in the data */
    FILE* fid = fopen(fName,"r");
    char buffer[200];
    fgets(buffer, 200, fid);
    while (fscanf(fid,"%lf,%lf",&(ptr->x),&(ptr->y)) == 2) {
        if (++dataRead == buf_size) {
            buf_size *= 2;
            data = realloc(data, buf_size*sizeof(data_t));
        }
        ptr = data+dataRead;
    }
    fclose(fid);
    
    /* shrink to correct size */
    data = realloc(data,dataRead*sizeof(data_t));

    if (dataRead == 0) {
        printf("no data read\n");
        return 0;
    } else {
        printf("%d data points read:\n",dataRead);
        for (int i = 0; i < dataRead; i++)
            printf(" %f,%f\n", data[i].x, data[i].y);
    }

    int rank = polyDegree+1;
    
    // allocate memory for the coefficient matrix
    double **A = (double**) malloc(rank*sizeof(double*));
    for (int i=0;i<rank;i++)
        A[i] = (double*) malloc(rank*sizeof(double));

    // allocate memory for the right-hand-side vector
    double *C = (double*) malloc(rank*sizeof(double));

    // allocate memory for the solution (the coefficients of the poly.)
    double *X = (double*) malloc(rank*sizeof(double));
  
    // fill-in the coefficient elements of A
    double sum;
    for(int i=0; i<rank; i++){
        for(int j=0; j<rank; j++){
            sum=0;
            for(int k=0; k<dataRead; k++)
                sum += pow(data[k].x,i+j);
            A[i][j] = sum;
        }
    }

    // fill-in the elements of right hand side vection
    for(int i=0; i<rank; i++){
        sum=0;
        for(int k=0; k<dataRead; k++)
            sum += pow(data[k].x,i)*data[k].y;
        C[i] = sum;
    }
    
    // solve for the coefficients 
    solveLU(A,X,C,rank);

    printf("\n Least square fit polynomial of degree %d \n\n p(x) = ",polyDegree);
    for(int i=0; i<rank; i++)
        printf("a[%d]*x^%d ",i,i);
    printf("\n\n");
    for(int i=0; i<rank; i++)
        printf(" a[%d] = %E\n",i,X[i]);
    
    // cleanup
    free(data);

    for (int i=0; i<rank; i++){
        free(A[i]);
    }
    free(A);
    free(X);
    free(C);

    return(0);
}
/*********************************************************************/
void solveLU(double** const A, double* X, double* const C, int n){

    //allocate memory for L, U and R for LU decomp method
    double **L = (double**) malloc(n*sizeof(double*));
    double **U = (double**) malloc(n*sizeof(double*));
    for (int i=0;i<n;i++){
        L[i] = (double*) malloc(n*sizeof(double));
        U[i] = (double*) malloc(n*sizeof(double));
    }
    double *R = (double*) malloc(n*sizeof(double));

    // Compute L and U from A = L*U 
    decomposeLU (A,L,U,n);

    // Compute R from L*R = C by forward substitution
    solveR (L,C,R,n);

    // Compute X from U*X = R by backward substitution
    solveX (U,R,X,n);

    //cleanup
    for (int i=0; i<n; i++){
        free(L[i]);
        free(U[i]);
    }
    free(L);
    free(U);
    free(R);
    
    return;
}

/*********************************************************************/
void decomposeLU (double** A, double** L, double** U, int n){

    double sum;
    
    for (int i=0; i<n; i++)
        L[i][0] = A[i][0] ;
    
    assert(fabs(L[0][0]) > SMALL);
    for (int j=1; j<n; j++)
        U[0][j] = A[0][j]/L[0][0] ;
    
    for (int j=1; j<n; j++) {
        for (int i=j; i<n; i++) {
            sum = 0;
            for (int k=0; k<j; k++) {
                sum += L[i][k] * U[k][j];
            }
            L[i][j] = A[i][j] - sum;
        }
        
        for (int k=j+1; k<n; k++) {
            sum = 0;
            for(int i=0; i<j; i++) {
                sum += L[j][i] * U[i][k];
            }
            assert(fabs(L[j][j]) > SMALL);
            U[j][k] = (A[j][k] - sum) / L[j][j];
        }
    }
    
    for (int i=0; i<n; i++)
        U[i][i] = 1;
    
    return ;
}

/*********************************************************************/
void solveX (double** const U, double* const R, double* X, int n){

    double sum;
    X[n-1] = R[n-1];
    for (int i=n-2; i>=0; i--) {
        sum = 0;
        for(int j=i+1; j<n; j++) {
            sum += U[i][j] * X[j];
        }
        X[i] = R[i] - sum;
    }
    
    return ;
}
/*********************************************************************/
void solveR (double** const L, double* const C, double* R, int n){

    double sum;
    
    R[0] = C[0]/L[0][0];
    for (int i=1; i<n; i++) {
        sum = 0;
        for(int j=0; j<i; j++) {
            sum += L[i][j] * R[j];
        }
        R[i] = (C[i] - sum) / L[i][i];
    }
    
    return ;
}
/*********************************************************************/

