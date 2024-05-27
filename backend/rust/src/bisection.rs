use serde::{Serialize, Deserialize};
use bigdecimal::{BigDecimal, ToPrimitive};
use meval::Expr;
use std::str::FromStr;

#[derive(Serialize, Deserialize)]
pub struct BisectionResponse {
    pub message: String,
    pub x_vals: Vec<BigDecimal>,
    pub f_vals: Vec<f64>,
    pub errors: Vec<BigDecimal>,
    pub iterations: Vec<usize>,
}

pub fn bisection(
    function_expression: &str,
    lower_bound: f64,
    upper_bound: f64,
    error_type: u8,
    tolerance_value: f64,
    max_iterations: usize,
) -> BisectionResponse {
    let function = Expr::from_str(function_expression).unwrap().bind("x").unwrap();

    let calculate = |x: f64| function(x);

    let function_at_lower_bound = calculate(lower_bound);
    let function_at_upper_bound = calculate(upper_bound);
    let tolerance = BigDecimal::from_str(&format!("{}", 0.5 / 10f64.powf(tolerance_value))).unwrap();

    if function_at_lower_bound == 0.0 {
        return BisectionResponse {
            message: format!("{} is a root of f(x)", lower_bound),
            x_vals: vec![],
            f_vals: vec![],
            errors: vec![],
            iterations: vec![],
        };
    } else if function_at_upper_bound == 0.0 {
        return BisectionResponse {
            message: format!("{} is a root of f(x)", upper_bound),
            x_vals: vec![],
            f_vals: vec![],
            errors: vec![],
            iterations: vec![],
        };
    } else if function_at_lower_bound * function_at_upper_bound > 0.0 {
        return BisectionResponse {
            message: "The interval is inadequate".to_string(),
            x_vals: vec![],
            f_vals: vec![],
            errors: vec![],
            iterations: vec![],
        };
    }

    let mut x_values = Vec::new();
    let mut function_values = Vec::new();
    let mut errors = Vec::new();
    let mut iterations = Vec::new();

    let mut iteration_count = 0;
    let mut lower = BigDecimal::from_str(&lower_bound.to_string()).unwrap();
    let mut upper = BigDecimal::from_str(&upper_bound.to_string()).unwrap();
    let mut mid_point = (&lower + &upper) / 2.into();
    let mut function_at_mid_point = calculate(mid_point.to_f64().unwrap());
    x_values.push(mid_point.clone());
    function_values.push(function_at_mid_point);
    errors.push(BigDecimal::from(100.0)); // Initial error set to 100%
    iterations.push(iteration_count);

    let mut previous_mid_point = mid_point.clone();

    while iteration_count < max_iterations {
        iteration_count += 1;

        let temp_x = calculate(lower.to_f64().unwrap()) * function_at_mid_point;

        if temp_x < 0.0 || temp_x == 0.0 {
            upper = mid_point.clone();
        } else {
            lower = mid_point.clone();
        }

        mid_point = (&lower + &upper) / 2.into();
        function_at_mid_point = calculate(mid_point.to_f64().unwrap());

        let error = if error_type == 1 {
            (&mid_point - &previous_mid_point).abs()
        } else {
            (&mid_point - &previous_mid_point).abs() / mid_point.abs()
        };

        x_values.push(mid_point.clone());
        function_values.push(function_at_mid_point);
        errors.push(error.clone());
        iterations.push(iteration_count);

        if error < tolerance {
            break;
        }

        previous_mid_point = mid_point.clone();
    }

    let message = if function_at_mid_point == 0.0 {
        format!("{} is a root of f(x)", mid_point)
    } else if errors[iteration_count - 1] < tolerance {
        format!("The approximate solution is: {}, with a tolerance = {}", mid_point, tolerance)
    } else {
        format!("Failed in {} iterations", max_iterations)
    };

    BisectionResponse {
        message,
        x_vals: x_values,
        f_vals: function_values,
        errors,
        iterations,
    }
}