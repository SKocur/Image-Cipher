extern crate image;

use std::io::prelude::*;
use std::fs::File;
use image::ColorType;
use image::ImageFormat;
use image::ImageBuffer;
use image::Rgb;

fn main() {
    let file_name = String::from("Editing How I redesigned LSB steganography algorithm_ – Medium.pdf");
    let mut file = File::open(&file_name)
        .expect("Cannot read file");

    let mut buffer = Vec::new();
    file.read_to_end(&mut buffer)
        .expect("Cannot load data to buffer");

    let mut size_y: u32 = 500;

    let size_x: u32 = buffer.len() as u32 / size_y;
    let mut image = ImageBuffer::<Rgb<u8>, Vec<u8>>::new(size_x, size_y);

    let mut counter: usize = 0;
    for y in 0..size_y - 1 {
        for x in 0..size_x {
            if counter == buffer.len() {
                break;
            } else if counter + 1 == buffer.len() {
                image.get_pixel_mut(x, y).data = [buffer[counter], 0, 0];
                break;
            } else if counter + 2 == buffer.len() {
                image.get_pixel_mut(x, y).data = [buffer[counter], buffer[counter + 1], 0];
                break;
            } else {
                image.get_pixel_mut(x, y).data = [buffer[counter], buffer[counter + 1], buffer[counter + 2]];
            }

            counter += 3;
        }
    }

    image.save(format!("{}.png", file_name)).unwrap();
}
