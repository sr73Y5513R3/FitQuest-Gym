import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../Services/login/auth-service.service';


export const authTokenInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  
  const authService = inject(AuthService);


  const authToken = authService.getToken(); 


  if (authToken) {
    
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`
      }
    });
    
    return next(authReq);
  }

  
  return next(req);
};
