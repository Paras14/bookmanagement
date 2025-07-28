import type React from "react"
import type { Metadata } from "next"
import { Inter, Rubik, Oswald } from "next/font/google"
import "./globals.css"

const inter = Inter({ subsets: ["latin"] })
const rubik = Rubik({ subsets: ["latin"], variable: "--font-rubik" })
const oswald = Oswald({ subsets: ["latin"], variable: "--font-oswald" })

export const metadata: Metadata = {
  title: "Library Books",
  description: "Manage your personal library collection",
    generator: 'v0.dev'
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={`${inter.className} ${rubik.variable} ${oswald.variable} min-h-screen flex flex-col`}>
        {children}
      </body>
    </html>
  )
}
