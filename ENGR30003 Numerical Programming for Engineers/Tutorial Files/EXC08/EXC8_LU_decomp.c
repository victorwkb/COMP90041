/* Solution to a system of linear equations
 *   by LU decomposition
 * 
 * input: for example for a 4x4 system
 *  n = 4    
 *  1 2 3 4  
 *  5 6 7 8
 *  7 8 9 1
 *  1 2 1 3
 *  10
 *  11
 *  12
 *  25
 *
 * written by Aman Kidanemariam, 17/09/2020 
 */
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <assert.h>

#define SMALL 1e-10

#define N 20

void decomposeLU (double const A[N][N], double L[N][N], double U[N][N], int n);
void solveR (double const L[N][N], double const C[N], double R[N], int n);
void solveX (double const U[N][N], double const R[N], double X[N], int n);
void print_matrix (double const mat[N][N], int n);
void print_vector (double const vec[N], int n);

int main(){
    
    double A[N][N]; // allocate some working buffers
    double L[N][N];
    double U[N][N];
    double C[N];
    double X[N];
    double R[N];
    
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

    // Compute L and U from A = L*U 
    decomposeLU (A,L,U,n);

    // Compute R from L*R = C by forward substitution
    solveR (L,C,R,n);
    
    // Compute X from U*X = R by backward substitution
    solveX (U,R,X,n);
    
    printf("\n-- A --  \n");
    print_matrix (A,n);
    printf("\n-- C --  \n");
    print_vector (C,n);
    printf("\n-- L --  \n");
    print_matrix (L,n);
    printf("\n-- U --  \n");
    print_matrix (U,n);
    printf("\n-- R --  \n");
    print_vector (R,n);
    printf("\n-- X --  \n");
    print_vector (X,n);
    
    return(0);
}

/*********************************************************************/
void decomposeLU (double const A[N][N], double L[N][N], double U[N][N], int n){

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
void solveX (double const U[N][N], double const R[N], double X[N], int n){

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
void solveR (double const L[N][N], double const C[N], double R[N], int n){

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
