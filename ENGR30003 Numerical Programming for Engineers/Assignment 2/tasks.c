/***************************************************************************
 *
 *   File        : tasks.c
 *   Student Id  : 981436
 *   Name        : Kai Bing Goh
 *
 ***************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <string.h>
#include "tasks.h"

#define BUFFER_MULTIPLIER 2.0
#define DEG 180.0
#define EPS 1.0E-6
#define LOWER_BOUND 0.125
#define MAXITER 1000
#define MAXTHETA 90
#define PI 3.14159265358979323846
#define OFF 0
#define ON 1
#define UPPER_BOUND 0.375

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

typedef struct
{
    double a;
    double b;
    double c;
    double r;
    double x;
} array_t;

typedef struct
{
    double x;
    double fx;
    double a;
    double b;
    double c;
    double d;
    double h;
    double Si;
} interp_t;

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

double deg_to_rad(double deg)
{
    double rad = deg / DEG * PI;
    return rad;
}
double rad_to_deg(double rad)
{
    double deg = rad * DEG / PI;
    return deg;
}

double fb_func(double M, double b, double gamma, double theta)
{
    double fb = 2 * (pow(M, 2) * pow(sin(b), 2) - 1) /
                    (tan(b) * (pow(M, 2) * (gamma + cos(2 * b)) + 2)) -
                tan(deg_to_rad(theta));
    return fb;
}

double dfb_func(double M, double b, double gamma)
{
    double term1 = 0, term2 = 0, term3 = 0;
    term1 = (4 * pow(M, 2) * sin(2 * b) * (pow(M, 2) * pow(sin(b), 2) - 1)) / (tan(b) * pow((pow(M, 2) * (gamma + cos(2 * b)) + 2), 2));
    term2 = 2 * (pow(tan(b), 2) + 1) * (pow(M, 2) * pow(sin(b), 2) - 1) / (pow(tan(b), 2) * ((gamma + cos(2 * b)) * pow(M, 2) + 2));
    term3 = (4 * pow(M, 2) * cos(b) * sin(b)) / (tan(b) * ((gamma + cos(2 * b)) * pow(M, 2) + 2));
    return term1 - term2 + term3;
}

// Root finding using Newton Raphson
double n_r(double b, double M, double theta, double gamma)
{
    int i = 0;
    while (fabs(b) > EPS && i < MAXITER)
    {
        b = b - fb_func(M, b, gamma, theta) / dfb_func(M, b, gamma);
        i++;
    }
    return b;
}

/* TASK 1 */
void shockwave(const char *q1_file)
{
    size_t buff = 1, n_lines = 0;
    double M = 0, theta = 0, gamma = 0, beta_l = 0, beta_u = 0, M2 = 0;
    FILE *fread1 = safe_fopen(q1_file, "r");
    // Reading values from input csv file
    while ((fgetc(fread1)) != '\n')
    {
    }
    fscanf(fread1, "%lf,%lf,%lf,%lf,%lf\n", &M, &theta, &beta_l, &beta_u, &gamma);
    while ((fgetc(fread1)) != '\n')
    {
    }
    double *mach = (double *)safe_malloc(sizeof(double));
    while (fscanf(fread1, "%lf\n", &M2) == 1)
    {
        if (n_lines == buff - 1)
        {
            buff = buff * BUFFER_MULTIPLIER;
            mach = safe_realloc(mach, buff * sizeof(double));
        }
        mach[n_lines] = M2;
        n_lines++;
    }
    fclose(fread1);

    // Newton Raphson method
    // Part A (finding Bl and Bu for M = 4.5 and theta = 26(deg))
    double b_u = 0, b_l = 0;
    // Change "OFF" to "ON" for outputting Part A's required answer into a csv file
    if (OFF)
    {
        FILE *fp1_sol = safe_fopen("out_shock_Asol.csv", "w");
        // b_l is approximately 0.8(radians) and 
        // b_u is approximately 1.4(radians) by estimating using the graphical solution
        // Both selected b_l and b_u values are close to roots of f(b) = 0 for M = 4.5, theta = 26
        double M_sol = 4.5, theta_sol = 26, b_u_sol = 1.4, b_l_sol = 0.8;
        b_l_sol = rad_to_deg(n_r(b_l_sol, M_sol, theta_sol, gamma));
        b_u_sol = rad_to_deg(n_r(b_u_sol, M_sol, theta_sol, gamma));
        fprintf(fp1_sol, "beta_lower,beta_upper\n%.4lf,%.4lf", b_l_sol, b_u_sol);
        fclose(fp1_sol);
    }

    // Part B
    FILE *fp1 = safe_fopen("out_shock.csv", "w");
    beta_l = deg_to_rad(beta_l);
    beta_u = deg_to_rad(beta_u);
    fprintf(fp1, "M,theta,beta_lower,beta_upper");
    for (size_t i = 0; i < n_lines; i++)
    {
        for (int theta = 0; theta < MAXTHETA / 2; theta++)
        {
            b_l = asin(1.0 / mach[i]);
            b_u = deg_to_rad(MAXTHETA);

            b_l = rad_to_deg(n_r(b_l, mach[i], theta, gamma));
            b_u = rad_to_deg(n_r(b_u, mach[i], theta, gamma));

            if (b_l >= 0 && (int)b_l <= MAXTHETA && b_u >= 0 && (int)b_u <= MAXTHETA)
            {
                fprintf(fp1, "\n%.4lf,%.4lf,%.4lf,%.4lf", mach[i], (double)theta, b_l, b_u);
            }
        }
    }
    fclose(fp1);
}

// Thomas algorithm for TASK 3
array_t alg(array_t *array, size_t n_points)
{
    for (int i = 0; i < n_points; i++)
    {
        if (i > 0)
        {
            int j = i - 1;
            array[i].b = array[i].b - (array[i].a * array[j].c) / array[j].b;
            array[i].r = array[i].r - (array[i].a * array[j].r) / array[j].b;
        }
    }
    for (int i = n_points - 1; i >= 0; i--)
    {
        if (i == n_points - 1)
        {
            array[i].x = array[i].r / array[i].b;
        }
        else
        {
            int j = i + 1;
            array[i].x = (array[i].r - array[i].c * array[j].x) / array[i].b;
        }
    }
    return *array;
}

/* TASK 3 */
void linsolve(const char *q3_file)
{
    FILE *fread3 = safe_fopen(q3_file, "r");
    size_t buff = 1, n_lines = 0;
    array_t *array = (array_t *)safe_malloc(sizeof(array_t));
    double a = 0, b = 0, c = 0, r = 0;
    // Reading values from input csv file
    while ((fgetc(fread3)) != '\n')
    {
    }
    while (fscanf(fread3, "%lf,%lf,%lf,%lf\n", &a, &b, &c, &r) == 4)
    {
        if (n_lines == buff - 1)
        {
            buff = buff * BUFFER_MULTIPLIER;
            array = safe_realloc(array, buff * sizeof(array_t));
        }
        array[n_lines].a = a;
        array[n_lines].b = b;
        array[n_lines].c = c;
        array[n_lines].r = r;
        n_lines++;
    }
    fclose(fread3);

    // Apply Thomas algorithm to the array structure
    alg(array, n_lines);

    // Printing results to output csv file
    FILE *fp3 = safe_fopen("out_linsolve.csv", "w");
    fprintf(fp3, "x");
    for (size_t i = 0; i <= n_lines - 1; i++)
    {
        fprintf(fp3, "\n%.6lf", array[i].x);
    }
    fclose(fp3);
}

/* TASK 4 */
void interp(const char *q4_file, double xo)
{
    FILE *fread4 = safe_fopen(q4_file, "r");
    double x = 0, fx = 0;
    size_t buff = 1, n_lines = 0;
    interp_t *data = (interp_t *)safe_malloc(sizeof(interp_t));
    // Reading values from input csv file
    while ((fgetc(fread4) != '\n'))
    {
    }
    while (fscanf(fread4, "%lf,%lf\n", &x, &fx) == 2)
    {
        if (n_lines == buff - 1)
        {
            buff = buff * BUFFER_MULTIPLIER;
            data = safe_realloc(data, buff * sizeof(interp_t));
        }
        data[n_lines].x = x;
        data[n_lines].a = fx;
        n_lines++;
    }

    size_t N = n_lines - 1;
    // Finding hi
    for (size_t i = 0; i < N; i++)
    {
        size_t n = i + 1;
        data[i].h = data[n].x - data[i].x;
    }
    // Finding ci
    array_t *array = (array_t *)safe_malloc(n_lines * sizeof(array_t));
    for (size_t i = 0; i < n_lines; i++)
    {
        if (i == 0 || i == n_lines - 1)
        {
            array[i].a = 0;
            array[i].b = 1;
            array[i].c = 0;
            array[i].r = 0;
        }
        else
        {
            array[i].a = data[i - 1].h;
            array[i].b = 2 * (data[i - 1].h + data[i].h);
            array[i].c = data[i].h;
            array[i].r = 3.0 * (data[i + 1].a - data[i].a) / data[i].h +
                         3.0 * (data[i - 1].a - data[i].a) / data[i - 1].h;
        }
    }

    // Apply Thomas algorithm on array structure
    alg(array, n_lines);

    for (size_t i = 0; i < n_lines; i++)
    {
        data[i].c = array[i].x;
    }
    // Finding bi
    for (size_t i = 0; i < N; i++)
    {
        size_t next = i + 1;
        data[i].b = (data[next].a - data[i].a) / data[i].h -
                    data[i].h * (2.0 * data[i].c + data[next].c) / 3.0;
    }
    //Finding di
    for (size_t i = 0; i < N; i++)
    {
        size_t next = i + 1;
        data[i].d = (data[next].c - data[i].c) / (3.0 * data[i].h);
    }

    // Finding interval and evaluating Si(x)
    size_t sibuff = 1, si_nlines = 0;
    double *si_val = (double *)safe_malloc(sizeof(double));
    for (size_t i = 0; i < N; i++)
    {
        size_t next = i + 1;
        if ((xo >= data[i].x && xo <= data[next].x) ||
            (xo >= data[next].x && xo <= data[i].x))
        {
            double x = data[i].x;
            if (si_nlines == sibuff - 1)
            {
                sibuff = sibuff * BUFFER_MULTIPLIER;
                si_val = safe_realloc(si_val, sibuff * sizeof(double));
            }
            si_val[si_nlines] = data[i].a + data[i].b * (xo - x) +
                                data[i].c * pow((xo - x), 2) +
                                data[i].d * pow((xo - x), 3);
            si_nlines++;
        }
    }
    // Printing Si(x) to output csv file
    FILE *fp4 = safe_fopen("out_interp.csv", "w");
    fprintf(fp4, "xo,f(xo)");
    for (size_t i = 0; i < si_nlines; i++)
    {
        fprintf(fp4, "\n%.6lf,%.6lf", xo, si_val[i]);
    }
    fclose(fp4);

    // Report's Si plot
    // Change OFF(0) to ON(1) to enable plotting for the report section of task 4
    if (OFF)
    {
        FILE *plotspline = safe_fopen("plotspline.csv", "w");
        for (size_t i = 0; i < N; i++)
        {
            fprintf(plotspline, "%.4lf,%.4lf,%.4lf,%.4lf,%.4lf\n", data[i].a, data[i].b, data[i].c, data[i].d, data[i].x);
        }
        fprintf(plotspline, "0,0,0,0,%.4lf\n", data[N].x);
        int n_si=n_lines;
        fprintf(plotspline, "N=%d", n_si);
        fclose(plotspline);
    }
}

/* TASK 5 */
void advection(const char *q5_file)
{
    double u = 0, cfl = 0, t_final = 0;
    int nx = 0;
    FILE *fread5 = safe_fopen(q5_file, "r");
    // Reading values from input csv file
    while ((fgetc(fread5) != '\n'))
    {
    }
    fscanf(fread5, "%lf,%d,%lf,%lf\n", &u, &nx, &cfl, &t_final);
    fclose(fread5);

    double delta_x = 1.0 / nx;
    double x[nx + 1];
    double delta_t = cfl * delta_x / u;

    // Calculating required number of iterations
    double phi_i[nx];
    double phi_next[nx];
    double phi_old[nx];
    int nt = ceil(t_final / delta_t);

    // Apply initial conditions: t = 0, n = 0
    for (size_t i = 0; i < nx + 1; i++)
    {
        x[i] = delta_x * i;
        if (LOWER_BOUND <= x[i] && x[i] <= UPPER_BOUND)
        {
            phi_i[i] = 0.5 * (1.0 - cos(8.0 * PI * (x[i] - LOWER_BOUND)));
        }
        else
        {
            phi_i[i] = 0;
        }
    }

    // Exact Solution Output
    // Change "OFF" to "ON" for outputting the exact solution for task 5 graph
    if (OFF)
    {
        FILE *fp_sol = safe_fopen("Exact_solution.csv", "w");
        double phi_sol[nx];
        for (size_t i = 0; i < nx + 1; i++)
        {
            x[i] = delta_x * i;
            if (LOWER_BOUND <= x[i] && x[i] <= UPPER_BOUND)
            {
                phi_sol[i] = 0.5 * (1.0 - cos(8.0 * PI * (fmod(x[i] - u * t_final, 1) - 0.125)));
            }
            else
            {
                phi_sol[i] = fmod(0 - u * t_final, 1);
            }
            fprintf(fp_sol, "%lf,%lf\n", delta_x * i, phi_sol[i]);
        }
        fclose(fp_sol);
    }

    // Algorithm for output for values read from input csv files
    for (size_t n = 0; n < nt; n++)
    {
        if (n == 0)
        {
            for (size_t i = 0; i <= nx; i++)
            {
                // Apply "Periodic" boundary conditions
                if (i == 0 || i == nx)
                {
                    phi_next[i] = phi_i[i] + delta_t * (-u * (phi_i[1] - phi_i[nx - 1]) / (2 * delta_x));
                }
                else
                {
                    phi_next[i] = phi_i[i] + delta_t * (-u * (phi_i[i + 1] - phi_i[i - 1]) / (2 * delta_x));
                }
            }
            // Storing only the past 2 rows
            for (size_t i = 0; i <= nx; i++)
            {
                phi_old[i] = phi_i[i];
                phi_i[i] = phi_next[i];
            }
        }
        else
        {
            for (size_t i = 0; i <= nx; i++)
            {
                // Apply "Periodic" boundary conditions
                if (i == 0 || i == nx)
                {
                    phi_next[i] = phi_old[i] + 2 * delta_t * (-u * (phi_i[1] - phi_i[nx - 1]) / (2 * delta_x));
                }
                else
                {
                    phi_next[i] = phi_old[i] + 2 * delta_t * (-u * (phi_i[i + 1] - phi_i[i - 1]) / (2 * delta_x));
                }
            }
            // Storing only the past 2 rows
            for (size_t i = 0; i <= nx; i++)
            {
                phi_old[i] = phi_i[i];
                phi_i[i] = phi_next[i];
            }
        }
    }

    // Printing results to output csv file
    FILE *fp5 = safe_fopen("out_advection.csv", "w");
    fprintf(fp5, "x,phi");
    for (size_t i = 0; i <= nx; i++)
    {
        fprintf(fp5, "\n%.6lf,%.6lf", delta_x * i, phi_next[i]);
    }
    fclose(fp5);
}