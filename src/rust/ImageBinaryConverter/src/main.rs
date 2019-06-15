extern crate image;

use std::env;
use std::io::prelude::*;
use std::fs::File;
use std::iter::FromIterator;
use image::ImageBuffer;
use image::Rgb;

const BYTES_PER_PIXEL: u8 = 3;

fn main() {
    let args: Vec<String> = env::args().collect();

    if args.len() > 1 {
        encrypt_vector_files(Vec::from_iter(args[1..].iter().cloned()));
    }
}

fn encrypt_vector_files(files: Vec<String>) {
    for file in files {
        encrypt_file(file);
    }
}

fn encrypt_file(file_name: String) {
    let mut file = File::open(&file_name)
        .expect("Cannot read file");

    let mut buffer = Vec::new();
    file.read_to_end(&mut buffer)
        .expect("Cannot load data to buffer");

    let bytes_to_pixels: f32 = buffer.len() as f32 / BYTES_PER_PIXEL as f32;
    let size_x = (bytes_to_pixels / 2.0) as u32;
    let size_y = (bytes_to_pixels - size_x as f32) as u32;
    println!("Bytes ({}) = {} x {} pixels", buffer.len() as u32, size_x, size_y);
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

    println!("Image processed");

    // Method below doesn't save jpg images, there is no eror, just no output
    image.save(format!("{}.png", file_name)).expect("Error while saving file");
}