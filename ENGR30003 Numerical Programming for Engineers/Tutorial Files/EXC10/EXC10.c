#include <stdio.h>
#include <math.h>

#define PI 3.141592653589793

int main (){

    double dx[] = {1./50.,1./100.,1./200.,1./400.};
    double u1a_error[4];
    double u2a_error[4];
    double u3a_error[4];
    double x[5], u[5];
    
    double xa = 0;
    double ua = cos(4*PI*xa);

    for (int i = 0; i<4; i++){
        for (int xi = 0; xi<5; xi++){
            x[xi] = (xi-0.5)*dx[i];
            u[xi] = cos(4*PI*x[xi]);
        }
        u1a_error[i] = 0.5*(u[0]+u[1]) - ua;
        u2a_error[i] = 3./8.*u[0] +3./4.*u[1] -1./8.*u[2] - ua;
        u3a_error[i] = 35./16.*u[1] -35./16.*u[2] + 21./16.*u[3] -5./16.*u[4] - ua;
    }

    for (int i = 0; i<4; i++)
        printf(" dx = %E  u1 error = %E\n",dx[i],fabs(u1a_error[i]));
    printf("\n");
    for (int i = 0; i<4; i++)
        printf(" dx = %E  u1 error = %E\n",dx[i],fabs(u2a_error[i]));
    printf("\n");
    for (int i = 0; i<4; i++)
        printf(" dx = %E  u1 error = %E\n",dx[i],fabs(u3a_error[i]));
    
    return 0;
}
