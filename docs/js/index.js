(function () {

    // topic for plugins using Uracle, shown in
    // our webpage
    const topic = "uracle-plugin";
    const container = document.getElementById("repositories");
    let currentPage = 1;

    function fetchReadme(owner, repo, branch) {
        return Promise.any(["README.md", "readme.md"].map(file => new Promise((resolve, reject) => {
            fetch(`https://raw.githubusercontent.com/${owner}/${repo}/${branch}/${file}`)
                    .then(response => response.text())
                    .then(text => {
                        if (text === "404: Not Found") {
                            reject("Not Found");
                        } else {
                            resolve(text);
                        }
                    });
        })));
    }

    /**
     *
     * @param {object} repository
     * @param {string} docs
     * @return {HTMLElement}
     */
    function createElement(repository, docs) {
        const element = document.createElement("div");
        const header = document.createElement("div");
        const body = document.createElement("div");

        header.innerHTML = DOMPurify.sanitize(`
        <p class="name">${repository["full_name"]}</p>
        <p>${repository["description"]}</p>
        <div>
            <span>${repository["stargazers_count"]} <i class="fas fa-star"></i></span>
            <span>${repository["forks_count"]} <i class="fas fa-code-branch"></i></span>
        </div>
        `);


        header.classList.add("header");

        body.classList.add("body", "hidden");
        body.innerHTML = DOMPurify.sanitize(marked(docs), { USE_PROFILES: { html: true } });

        element.classList.add("repository");
        element.append(header, body);

        header.addEventListener("click", () => body.classList.toggle("hidden"));

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
                        fetchReadme(item["owner"]["login"], item["name"], item["default_branch"])
                                .then(readme => container.appendChild(createElement(item, readme)));
                    }
                });
    }

    // initial
    fetchRepositories(currentPage);

})();