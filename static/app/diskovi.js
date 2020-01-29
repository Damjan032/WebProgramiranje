Vue.use(VueRouter);
const diskovi = { template: '<diskovi></diskovi>' }
const detaljiDiska = { template: '<detalji-diska></detalji-diska>' }
const dodajDIsk = { template: '<dodaj-disk></dodaj-disk>' }

const router = new VueRouter({
    routes: [
        { path: '/',
            component: diskovi
        },
        { path: '/dodajDisk',
            component: dodajDIsk
        },  
        { path: '/detaljiDiska/:disk/:tipKorisnika',
            name:"detaljiDiska",
            component: detaljiDiska,
            props: true 
        }
    ]
});

let diskapp = new Vue({
    router,
    el:"#diskapp",
    data:{
        selDisk:null
    },
    
    methods:{

    }
});