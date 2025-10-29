#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Script para optimizar imágenes de doctores
Reduce el tamaño y convierte a formato óptimo para Android
"""

import sys
from pathlib import Path

try:
    from PIL import Image
    import os
except ImportError:
    print("Instalando Pillow...")
    import subprocess
    subprocess.check_call([sys.executable, "-m", "pip", "install", "Pillow"])
    from PIL import Image
    import os

# Configuración UTF-8 para Windows
if sys.platform == 'win32':
    sys.stdout.reconfigure(encoding='utf-8')

def optimize_image(input_path, output_path, max_size=200, quality=85):
    """
    Optimiza una imagen reduciendo su tamaño

    Args:
        input_path: Ruta de la imagen original
        output_path: Ruta de salida
        max_size: Tamaño máximo en KB (por defecto 200KB)
        quality: Calidad JPEG (1-100, por defecto 85)
    """
    try:
        img = Image.open(input_path)

        # Convertir RGBA a RGB si es necesario
        if img.mode in ('RGBA', 'LA', 'P'):
            background = Image.new('RGB', img.size, (255, 255, 255))
            if img.mode == 'P':
                img = img.convert('RGBA')
            background.paste(img, mask=img.split()[-1] if img.mode in ('RGBA', 'LA') else None)
            img = background

        # Redimensionar si es muy grande (máximo 512x512 para avatares)
        max_dimension = 512
        if img.width > max_dimension or img.height > max_dimension:
            img.thumbnail((max_dimension, max_dimension), Image.Resampling.LANCZOS)

        # Guardar con calidad optimizada
        img.save(output_path, 'JPEG', quality=quality, optimize=True)

        # Verificar tamaño y reducir calidad si es necesario
        file_size_kb = os.path.getsize(output_path) / 1024
        current_quality = quality

        while file_size_kb > max_size and current_quality > 50:
            current_quality -= 5
            img.save(output_path, 'JPEG', quality=current_quality, optimize=True)
            file_size_kb = os.path.getsize(output_path) / 1024

        return file_size_kb
    except Exception as e:
        print(f"Error procesando {input_path}: {e}")
        return None

def main():
    # Ruta de las imágenes
    drawable_path = Path(__file__).parent / "mobile" / "app" / "src" / "main" / "res" / "drawable-nodpi"

    if not drawable_path.exists():
        print(f"Error: No se encuentra la carpeta {drawable_path}")
        return

    print("Optimizando imágenes de doctores...")
    print("-" * 50)

    total_before = 0
    total_after = 0

    for i in range(1, 7):
        image_file = drawable_path / f"doctor_{i}.jpg"

        if not image_file.exists():
            print(f"Advertencia: No se encuentra {image_file}")
            continue

        # Tamaño original
        size_before = os.path.getsize(image_file) / 1024
        total_before += size_before

        # Crear backup temporal
        backup_file = drawable_path / f"doctor_{i}_backup.jpg"
        import shutil
        shutil.copy2(image_file, backup_file)

        # Optimizar
        size_after = optimize_image(image_file, image_file, max_size=50, quality=85)

        if size_after:
            total_after += size_after
            reduction = ((size_before - size_after) / size_before) * 100
            print(f"doctor_{i}.jpg: {size_before:.1f}KB -> {size_after:.1f}KB ({reduction:.1f}% reduccion)")

            # Eliminar backup si todo salió bien
            backup_file.unlink()
        else:
            # Restaurar backup si hubo error
            shutil.copy2(backup_file, image_file)
            backup_file.unlink()
            print(f"doctor_{i}.jpg: Error, restaurado desde backup")

    print("-" * 50)
    print(f"Total antes: {total_before:.1f}KB")
    print(f"Total despues: {total_after:.1f}KB")
    print(f"Reduccion total: {((total_before - total_after) / total_before * 100):.1f}%")
    print("\nImagenes optimizadas correctamente!")

if __name__ == "__main__":
    main()
