/* The bisection method for finding a root of an equation.
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define NITER 1000

typedef struct {
    double x;
    double fx;
    double eps;
    int iter;
} root_t;


double f(double);
root_t find_root(double,double,double,int,int);

int main(int argc, char *argv[]) {
	root_t root;
	double LEFT,RGHT;
	int method=0;
    
	printf("\nSpecify lower value (x_l): ");
    scanf("%lf",&LEFT);
	
	printf("\nSpecify upper value (x_u): ");
    scanf("%lf",&RGHT);

    while (method!=1 && method!=2){
        printf("\nChoose bracketing method [1 for 'bisection' or 2 for 'false position']: ");
        scanf("%d",&method);
    }
        
    double eps[] = {1e-6, 1e-8, 1e-10, 1e-12};
    for (int i = 0; i<4; i++){
        root = find_root(LEFT, RGHT, eps[i], NITER, method);
        printf("%s: x = %.12E, f(x) = %.12E eps=%.1E iterations=%d\n",
               method==1?"BISECTION:":"FALSE POSITION:",root.x,root.fx,root.eps,root.iter);
    }
    
	return 0;
}


double f(double x) {
    return 1000/x*(1-exp(-8*x/100))-50;
}

root_t find_root(double xl, double xu, double eps, int limit, int method) {

    double fxl, fxu, xr, fxr = 1;
    int iterations=0;
	fxl = f(xl);
	fxu = f(xu);
    
	while (fabs(fxr) >= eps && iterations < limit) {

        iterations = iterations+1;

        if (method==1)
            xr  = (xl+xu)/2.0;
        else if (method==2)
            xr = xl - fxl*(xu-xl)/(fxu-fxl);

		fxr = f(xr);
        
		if (fxl*fxr < 0) {/* root is to left of middle */
			xu  = xr;
			fxu = fxr;
		} else { /* root is to right */
			xl  = xr;
			fxl = fxr;
		}
	}
    if (iterations==1000 && fabs(fxr) >= eps)
        printf("WARNING: solution not converged!\n");
    
    root_t root;
    root.x  = xr;
    root.fx = fxr;
    root.eps = eps;
    root.iter = iterations;
	return root;
}

