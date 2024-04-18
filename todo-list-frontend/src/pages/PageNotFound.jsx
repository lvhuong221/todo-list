
function PageNotFound() {
    return (
        <div className="page-not-found-container">
            <p>
                Page not found
            </p>
            <button >
                <CustomLink to={"/"}>Return home</CustomLink>
            </button>
        </div>
    )
}

export default PageNotFound