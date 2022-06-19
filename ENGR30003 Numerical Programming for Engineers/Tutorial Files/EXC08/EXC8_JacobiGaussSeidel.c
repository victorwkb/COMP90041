/* Iterative solution to a system of linear equations
 *   Point Jacobi and Gauss-Seidel methods
 * 
 * input: for example for a 3x3 system
 *  n = 3
 *  1 2 3
 *  4 5 6
 *  7 8 9
 *  10
 *  11
 *  12
 * 
 * written by Aman Kidanemariam, 17/09/2020 
 */
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <assert.h>

#define NITER 100
#define EPS 1e-6

#define N 20

#define JACOBI 1
#define GAUSSSEIDEL 2

// Select here which method you want to use
int method = JACOBI;
//int method = GAUSSSEIDEL;


void solvePointJacobi (double A[N][N], double x[N], double C[N], double r[N], int n);
void solveGaussSeidel (double A[N][N], double x[N], double C[N], double r[N], int n);
double residual (double A[N][N], double x[N], double C[N], double r[N], int n);
void print_matrix (double const mat[N][N], int n);
void print_vector (double const vec[N], int n);

int main()
{
    double A[N][N]; // allocate some working buffers
    double C[N];
    double r[N];
    double x[N];
 
    int n;
  
    if (scanf("n = %d\n", &n) != 1) {
        printf("error reading n dimension.\n");
        exit(EXIT_FAILURE);
    }
    assert(n<=N);
    
    // read in the coefficient matrix
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (scanf("%lf", &(A[i][j])) != 1) {
                printf("error reading matrix A data.\n");
                exit(EXIT_FAILURE);
            }
        }
    }
    // read in the right hand side vector
    for (int i = 0; i < n; i++) {
        if (scanf("%lf", &(C[i])) != 1) {
            printf("error reading vector C data.\n");
            exit(EXIT_FAILURE);
        }
    }
    
    printf("\n-- A --  \n");
    print_matrix (A,n);
    printf("\n-- C --  \n");
    print_vector (C,n);
    
    // initial guess
    for(int i=0;i<n; i++)
        x[i] = 0;

    if (method==JACOBI)
        solvePointJacobi(A,x,C,r,n);
    else if (method==GAUSSSEIDEL)
        solveGaussSeidel(A,x,C,r,n);
    else{
        printf("Unknown method.\n");
        exit(0);
    }
    
    printf("\n-- X --  \n");
    print_vector (x,n);
    
    return(0);
}
/*********************************************************************/
void solvePointJacobi (double A[N][N], double x[N], double C[N], double r[N], int n){

    int iteration=0;
    double sum;
    double xold[N];
    
    double err = residual (A,x,C,r,n);
    printf("initial error = %f\n",err);
    
    while (err > EPS && iteration < NITER)  {
		iteration = iteration+1;

        for(int i=0;i<n; i++)
            xold[i] = x[i];
        
        for (int i=0; i < n; i++) {
            sum = 0;
            for (int j = 0; j < i; j++) {
                sum += A[i][j]*xold[j];
            }
            for (int j = i+1; j < n; j++) {
                sum += A[i][j]*xold[j];
            }
            x[i] = 1/A[i][i]*(C[i] - sum);
        }
        
        err = residual (A,x,C,r,n);
        printf("Point-Jacobi: iter = %d, max residual = %E\n",iteration,err);
    }
    
    return ;
}
/*********************************************************************/
void solveGaussSeidel(double A[N][N], double x[N], double C[N], double r[N], int n){

    int iteration=0;
    double sum;
    
    double err = residual (A,x,C,r,n);
    printf("initial error = %f\n",err);
    
    while (err > EPS && iteration < NITER)  {
		iteration = iteration+1;

        for (int i=0; i < n; i++) {
            sum = 0;
            for (int j = 0; j < i; j++) {
                sum += A[i][j]*x[j];
            }
            for (int j = i+1; j < n; j++) {
                sum += A[i][j]*x[j];
            }
            x[i] = 1/A[i][i]*(C[i] - sum);
        }
        
        err = residual (A,x,C,r,n);
        printf("Gauss-Seidel: iter = %d, max residual = %E\n",iteration,err);
    }
    
    return ;
}

/*********************************************************************/
double residual (double A[N][N], double x[N], double C[N], double r[N], int n) {

    double max_err = 0;
    /* Compute the residual A*x - C*/
    for (int i=0; i < n; i++) {
        r[i] = -C[i];
        for (int j = 0; j < n; j++) {
            r[i] += A[i][j]*x[j];	 
        }                      
        max_err = fmax(max_err,fabs(r[i]));
    }
    return max_err;
}

/*********************************************************************/
void print_matrix (double const mat[N][N], int n) {
    for (int i=0; i<n; i++){
        for (int j=0; j<n; j++)
            printf("%9.6f  ",mat[i][j]);
        printf("\n");
    }
    
    return;
}
void print_vector (double const vec[N], int n) {
    for (int i=0; i<n; i++)
        printf("%9.6f\n",vec[i]);
    return;
}
/*********************************************************************/
