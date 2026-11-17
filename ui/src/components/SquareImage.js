import React, { useEffect, useRef, useState } from 'react'
import cx from 'classnames'

export default function SquareImage({
  href,
  imgSrc,
  placeholder,
  className,
  imgClassName,
  // 'contain': the whole photo with a margin; 'cover': the photo fills the square (edges may be cut)
  fit = 'contain',
  paddingClassName = fit === 'cover' ? 'p-0' : 'p-2',
  ...restProps
}) {
  const containerRef = useRef(null)
  // null while unknown; then { fill } for a scene photo (see detectScene), false for a product photo
  const [scene, setScene] = useState(null)
  useEffect(() => setScene(null), [imgSrc])

  function handleLoad(e) {
    setScene(detectScene(e.target, containerRef.current))
  }

  const isScene = !!scene
  const objectFit = fit === 'cover' || (isScene && scene.fill) ? 'object-cover' : 'object-contain'

  return (
    <div
      {...restProps}
      ref={containerRef}
      className={cx(className, 'square-container position-relative overflow-hidden', {
        'square-container--scene': isScene
      })}
    >
      <a href={href}>
        {imgSrc ? (
          <>
            {isScene && objectFit === 'object-contain' && (
              // the same photo, blurred, fills the space the whole photo leaves free
              <img
                src={imgSrc}
                alt=""
                aria-hidden="true"
                className="square-image__backdrop position-absolute h-100 w-100 object-cover"
              />
            )}
            <img
              key={imgSrc}
              src={imgSrc}
              alt=""
              onLoad={handleLoad}
              className={cx(
                'position-absolute object-center img-fluid h-100 w-100',
                isScene ? 'bg-transparent' : 'bg-body',
                objectFit,
                imgClassName,
                isScene ? 'p-0' : paddingClassName
              )}
            />
          </>
        ) : placeholder ? (
          <div className="position-absolute h-100 w-100 d-flex justify-content-center align-items-center">
            <div>{placeholder}</div>
          </div>
        ) : (
          <ImgPlaceholder />
        )}
      </a>
    </div>
  )
}

function ImgPlaceholder() {
  return <div className={cx('ui-img-placeholder d-block position-absolute h-100 w-100 bg-light-shade')} />
}

// A product photo has a white or light gray background; a scene photo (e.g. a render of a room) fills the
// whole frame. Looks at the corners of the photo: when at least 3 of them are neither light nor transparent,
// it is a scene. Scenes are shown without margin and without the light frame of dark mode, and fill the box
// when their shape is close to it (a square photo in a square box); otherwise they are shown whole over a
// blurred copy of themselves. Photos that can't be read (another origin) count as products.
function detectScene(img, container) {
  try {
    const w = img.naturalWidth
    const h = img.naturalHeight
    if (!w || !h) return false
    const size = 16
    const canvas = document.createElement('canvas')
    canvas.width = size
    canvas.height = size
    const ctx = canvas.getContext('2d', { willReadFrequently: true })
    ctx.drawImage(img, 0, 0, size, size)
    const corners = [
      [0, 0],
      [size - 1, 0],
      [0, size - 1],
      [size - 1, size - 1]
    ]
    const darkCorners = corners.filter(([x, y]) => {
      const [r, g, b, a] = ctx.getImageData(x, y, 1, 1).data
      if (a < 200) return false // transparent background: a product
      const light = Math.min(r, g, b) > 200 && Math.max(r, g, b) - Math.min(r, g, b) < 30
      return !light
    }).length
    if (darkCorners < 3) return false

    let fill = false
    if (container && container.clientWidth && container.clientHeight) {
      const ratio = w / h / (container.clientWidth / container.clientHeight)
      fill = ratio > 0.83 && ratio < 1.2
    }
    return { fill }
  } catch (e) {
    return false
  }
}
