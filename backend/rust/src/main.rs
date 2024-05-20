use actix_web::{web, App, HttpResponse, HttpServer, Responder};
use serde_json::json;

async fn hello_world() -> impl Responder {
    HttpResponse::Ok().json(json!({"hello": "world"}))
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .route("/", web::get().to(hello_world))
    })
        .bind("127.0.0.1:8080")?
        .run()
        .await
}