import React from 'react'
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
  return (
    <div {...restProps} className={cx(className, 'square-container position-relative overflow-hidden')}>
      <a href={href}>
        {imgSrc ? (
          <img
            src={imgSrc}
            alt=""
            className={cx(
              'bg-body position-absolute object-center img-fluid h-100 w-100',
              fit === 'cover' ? 'object-cover' : 'object-contain',
              imgClassName,
              paddingClassName
            )}
          />
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
