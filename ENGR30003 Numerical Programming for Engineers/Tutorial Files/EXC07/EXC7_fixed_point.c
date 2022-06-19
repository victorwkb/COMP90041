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


double g1(double);
double g2(double);
double g3(double);

root_t fixedpoint(double, double, int, double (*g)(double));

int main() {
	root_t root;
	double LEFT;
    double eps;
    double (*g)(double);
    
	printf("\nSpecify intitial guess: ");
    scanf("%lf",&LEFT);

    printf("\nSpecify error tolerance: ");
    scanf("%lf",&eps);

    // choose the desired function for g(x)
    //g = &g1;
    //g = &g2;
    g = &g3;
    root = fixedpoint(LEFT,  eps, NITER, g);
    printf("x = %.12E, g(x) = %12.10E, eps=%.1E, iter=%d\n",root.x, g(root.x),root.eps,root.iter);
    
	return 0;
}

double g1(double x) {
	return sqrt(2.0*x+3.0);
}
double g2(double x) {
	return 3.0/(x-2.0);
}
double g3(double x) {
	return (x*x-3)/2.0;
}

root_t fixedpoint(double x1, double eps, int limit, double (*g)(double)) {
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
   Aman Kidanemariam 11.09.20
   =================== */

