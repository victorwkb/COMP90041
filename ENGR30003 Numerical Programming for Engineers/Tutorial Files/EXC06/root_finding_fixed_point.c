/* The Fixed-point iteration method for finding a root of an equation.
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define NITER 1000

typedef struct {
    double x;
    double eps;
    int iter;
} root_t;


double g(double);
root_t fixedpoint(double, double, int);

int main() {
	root_t root;
	double LEFT;
	
	printf("\nSpecify intitial guess: ");
    scanf("%lf",&LEFT);
	
	double eps[] = {1e-4, 1e-8, 1e-10, 1e-12};
    for (int i = 0; i<4; i++){
        root = fixedpoint(LEFT,  eps[i], NITER);
        printf("x = %.12E, g(x) = %12.10E, eps=%.1E, iter=%d\n",
               root.x, g(root.x),root.eps,root.iter);
    }
    
	return 0;
}

double g(double x) {
	return 0.1*exp(x);
    //return cos(x);
}

root_t fixedpoint(double x1, double eps, int limit) {
	double gx1;
	int iterations=0;
	gx1 = g(x1);	
    
	while (fabs(gx1-x1) > eps  && iterations < limit) {
		iterations = iterations+1;
        x1 = g(x1);
		gx1= g(x1);
	}
	
    root_t root;
    root.x  = x1;
    root.eps = eps;
    root.iter = iterations;
	return root;
}

/* =====================================================================
   Written by Richard Sandberg, 29/08/2016
   modif. Aman Kidanemariam 09.09.20
   =================== */

