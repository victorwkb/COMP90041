/* The Newton Raphson method for finding a root of an equation.
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

double f(double);
double fprime(double);
root_t newtonraph(double, double,int);

int main() {
	root_t root;
	double LEFT;
	double eps=1e-4;
    
    printf("\nSpecify intitial guess: ");
    scanf("%lf",&LEFT);

    root = newtonraph(LEFT,eps,NITER);
    printf("x = %.12E, f(x) = %12.10E, eps=%.1E, iter=%d\n",root.x, f(root.x),root.eps,root.iter);
    
	return 0;
}

double f(double x) {
	return pow(x,4) + 30*exp(-x/4)-60;
}

double fprime(double x) {
	return 4*pow(x,3) - 30*exp(-x/4)/4.0;
}

root_t newtonraph(double x1, double eps, int limit) {
	double fx1, fpx1;
	int iterations=0;
	fx1 = f(x1);	
	fpx1= fprime(x1);
    
	while (fabs(fx1) > eps && iterations < limit) {
		iterations = iterations+1;
		x1 = x1-fx1/fpx1;
		fx1 = f(x1);
		fpx1= fprime(x1);
	}

    root_t root;
    root.x  = x1;
    root.eps = eps;
    root.iter = iterations;
	return root;
}

/* =====================================================================
   Aman Kidanemariam 11.09.20
   ================================================================== */

