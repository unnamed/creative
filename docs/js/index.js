(function () {

    // topic for plugins using Uracle, shown in
    // our webpage
    const topic = "uracle-plugin";
    const container = document.getElementById("repositories");
    let currentPage = 1;

    /**
     *
     * @param {object} repository
     * @return {HTMLElement}
     */
    function createElement(repository) {
        const element = document.createElement("div");

        element.innerHTML = DOMPurify.sanitize(`
        <p class="name">${repository["full_name"]}</p>
        <p>${repository["description"]}</p>
        <div>
            <span>${repository["stargazers_count"]} <i class="fas fa-star"></i></span>
            <span>${repository["forks_count"]} <i class="fas fa-code-branch"></i></span>
        </div>
        `);

        element.addEventListener("click", () => window.open(repository["html_url"]));
        element.classList.add("repository");

        return element;
    }

    /**
     * Fetches and draws repositories with the configured
     * topic at the given page
     * @param {number} page Repositories page
     */
    function fetchRepositories(page) {
        // https://api.github.com/search/repositories?q={query}{&page,per_page,sort,order}
        fetch("https://api.github.com/search/repositories"
                + `?q=topic:${topic}`
                + `&sort=stars&page=${page}`)
                .then(response => response.json())
                .then(({ items }) => {
                    for (const item of items) {
                        container.appendChild(createElement(item));
                    }
                });
    }

    // initial
    fetchRepositories(currentPage);

    // menu collapse
    document.querySelector(".menu-toggle").addEventListener("click", () => {
        document.querySelector("aside").classList.toggle("collapsed");
    });

})();