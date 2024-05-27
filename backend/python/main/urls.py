from django.urls import path
from .views import bisection_method
from .views import false_rule_method
from .views import fixed_point_method
from .views import incremental_search_method
from .views import multiple_roots_method
from .views import newton_raphson_method
from .views import secant_method

from .views import cholesky_method
from .views import crout_method
from .views import doolittle_method
from .views import simple_gaussian_elimination_method
from .views import pivot_gaussian_elimination_method
from .views import lu_gaussian_elimination_method
from .views import jacobi_method
from .views import gauss_seidel_method

urlpatterns = [
    path('api/v1/false-rule', false_rule_method, name='false_rule_method'),
    path('api/v1/bisection', bisection_method, name='bisection_method'),
    path('api/v1/fixed-point', fixed_point_method, name='fixed_point_method'),
    path('api/v1/incremental-search', incremental_search_method, name='incremental_search_method'),
    path('api/v1/multiple-roots', multiple_roots_method, name='multiple_roots_method'),
    path('api/v1/newton-raphson', newton_raphson_method, name='newton_raphson_method'),
    path('api/v1/secant', secant_method, name='secant_method'),
    path('api/v1/cholesky', cholesky_method, name='cholesky_method'),
    path('api/v1/crout', crout_method, name='crout_method'),
    path('api/v1/doolittle', doolittle_method, name='doolittle_method'),
    path('api/v1/simple-gaussian-elimination', simple_gaussian_elimination_method, name='simple_gaussian_elimination_method'),
    path('api/v1/pivot-gaussian-elimination', pivot_gaussian_elimination_method, name='pivot_gaussian_elimination_method'),
    path('api/v1/lu-gaussian-elimination', lu_gaussian_elimination_method, name='lu_gaussian_elimination_method'),
    path('api/v1/jacobi', jacobi_method, name='jacobi_method'),
    path('api/v1/gauss-seidel', gauss_seidel_method, name='gauss_seidel_method'),
]