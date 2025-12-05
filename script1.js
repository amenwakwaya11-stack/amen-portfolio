
document.addEventListener('DOMContentLoaded', function() {
    const hamburger = document.querySelector('.hamburger');
    const navLinks = document.querySelector('.nav-links');
    
   
    hamburger.addEventListener('click', function() {
        navLinks.classList.toggle('active');
        
       
        const icon = hamburger.querySelector('i');
        if (navLinks.classList.contains('active')) {
            icon.classList.remove('fa-bars');
            icon.classList.add('fa-times');
        } else {
            icon.classList.remove('fa-times');
            icon.classList.add('fa-bars');
        }
    });
    
   
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', () => {
            navLinks.classList.remove('active');
            hamburger.querySelector('i').classList.remove('fa-times');
            hamburger.querySelector('i').classList.add('fa-bars');
        });
    });
    
   
    const filterButtons = document.querySelectorAll('.filter-btn');
    const portfolioItems = document.querySelectorAll('.portfolio-item');
    
    filterButtons.forEach(button => {
        button.addEventListener('click', () => {
          
            filterButtons.forEach(btn => btn.classList.remove('active'));
       
            button.classList.add('active');
            
            const filterValue = button.getAttribute('data-filter');
            
            portfolioItems.forEach(item => {
                const categories = item.getAttribute('data-category').split(' ');
                if (filterValue === 'all' || categories.includes(filterValue)) {
                    item.style.display = 'block';
                   
                    setTimeout(() => {
                        item.style.opacity = '1';
                        item.style.transform = 'translateY(0)';
                    }, 100);
                } else {
                    item.style.opacity = '0';
                    item.style.transform = 'translateY(20px)';
                    setTimeout(() => {
                        item.style.display = 'none';
                    }, 300);
                }
            });
        });
    });
    

    const animateSkillBars = () => {
        const skillLevels = document.querySelectorAll('.skill-level');
        
        skillLevels.forEach(skill => {
            const level = skill.getAttribute('data-level');
            skill.style.width = level + '%';
        });
    };
    

    const backToTopButton = document.querySelector('.back-to-top');
    
    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            backToTopButton.classList.add('active');
        } else {
            backToTopButton.classList.remove('active');
        }
    });
    
    backToTopButton.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
    

    const contactForm = document.getElementById('contactForm');
    
    contactForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
   
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const subject = document.getElementById('subject').value;
        const message = document.getElementById('message').value;
        
        
        if (!name || !email || !subject || !message) {
            alert('Please fill in all required fields.');
            return;
        }
        
       
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert('Please enter a valid email address.');
            return;
        }
        
    
        alert(`Thank you, ${name}! Your message has been sent successfully. I'll get back to you soon at ${email}.`);
        
     
        contactForm.reset();
    });
    
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 70,
                    behavior: 'smooth'
                });
            }
        });
    });
 
    const cvDownloadButton = document.querySelector('.btn-download');
    
    cvDownloadButton.addEventListener('click', function(e) {

        fetch(this.href)
            .then(response => {
                if (!response.ok) {
                   
                    e.preventDefault();
                    createFallbackCV();
                }
             
            })
            .catch(error => {
               
                e.preventDefault();
                createFallbackCV();
            });
    });
    
    function createFallbackCV() {
        console.log('Creating fallback CV...');
        
        const cvContent = `AMEN WAKWAYA - CURRICULUM VITAE

PERSONAL INFORMATION
Name: Amen Wakwaya
Email: amen.wakwaya@wku.edu.et
Phone: +251 91 234 5678
Location: Wolkite, Ethiopia
University: Wolkite University

EDUCATION
Bachelor of Information Technology
Wolkite University, Ethiopia
2021 - Present (Expected Graduation: 2027)

Relevant Coursework:
• Web Development and Design
• Database Management Systems
• Object-Oriented Programming
• Data Structures and Algorithms
• Internet Programming
• Software Engineering

TECHNICAL SKILLS
Programming Languages:
• Java
• Python  
• C#
• JavaScript
• HTML5/CSS3

Tools & Technologies:
• MySQL Database
• Visual Studio IDE
• Visual Studio Code
• Responsive Web Design
• Problem Solving

CERTIFICATIONS
• Python Programming - Red Hat Certification (2023)

ACADEMIC PROJECTS
1. Wolkite University Employee Management System
   - Developed using C# programming language
   - Created user-friendly interface for employee data management
   - Implemented data entry, search, and management features

2. C# Calculator Application
   - Built a fully functional calculator with arithmetic operations
   - Implemented error handling and input validation
   - Designed clean and intuitive user interface

3. C# Notepad Application
   - Created text editor with file operations (open, save, new)
   - Implemented text formatting and editing capabilities
   - Added file dialog functionality for enhanced user experience

PERSONAL PROJECT
Personal Portfolio Website
- Developed responsive website using HTML, CSS, and JavaScript
- Implemented interactive features including project filtering
- Created contact form with validation
- Designed mobile-friendly interface

LANGUAGE PROFICIENCY
• Amharic: Native Speaker
• English: Fluent (Reading, Writing, Speaking)

This CV was created for my Internet Programming course assignment at Wolkite University.`;

       
        const blob = new Blob([cvContent], { 
            type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'Amen-Wakwaya-CV.docx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
        
        alert('CV downloaded successfully! If you don\'t see the file, check your downloads folder.');
    }
    

    setTimeout(animateSkillBars, 500);
    

    const animateOnScroll = () => {
        const elements = document.querySelectorAll('.about-content, .portfolio-item, .skill-category, .contact-item, .resume-section');
        
        elements.forEach(element => {
            const elementPosition = element.getBoundingClientRect().top;
            const screenPosition = window.innerHeight / 1.3;
            
            if (elementPosition < screenPosition) {
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
            }
        });
    };
   
    const elements = document.querySelectorAll('.about-content, .portfolio-item, .skill-category, .contact-item, .resume-section');
    elements.forEach(element => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        element.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    });
  
    setTimeout(animateOnScroll, 100);
    
  
    window.addEventListener('scroll', animateOnScroll);
    
  
    window.addEventListener('load', function() {
        document.body.classList.add('loaded');
    });
});