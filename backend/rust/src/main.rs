mod bisection;

use actix_web::{web, App, HttpServer, Responder, HttpResponse};
use serde::Deserialize;
use bisection::bisection;

#[derive(Deserialize)]
pub struct BisectionRequest {
    pub function_expression: String,
    pub lower_bound: f64,
    pub upper_bound: f64,
    pub error_type: u8,
    pub tolerance_value: f64,
    pub max_iterations: usize,
}

async fn bisection_handler(req: web::Json<BisectionRequest>) -> impl Responder {
    let response = bisection(
        &req.function_expression,
        req.lower_bound,
        req.upper_bound,
        req.error_type,
        req.tolerance_value,
        req.max_iterations,
    );

    HttpResponse::Ok().json(response)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .route("/bisection", web::post().to(bisection_handler))
    })
        .bind("127.0.0.1:8080")?
        .run()
        .await
}